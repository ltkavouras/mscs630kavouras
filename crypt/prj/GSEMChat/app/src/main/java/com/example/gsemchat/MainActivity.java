package com.example.gsemchat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Author: Louis T. Kavouras
 * Course: MSCS 630 Security Algorithms
 * File: ProfileActivity.java
 * Version 3.0
 * Description: This is the main activity also the chat application this method encrypts, decrypts
 * all the data and sends it to the database for the user.
 */
public class MainActivity extends AppCompatActivity {

    // Declaration of views
    ImageView mChat, mProfile, mSettings;
    private EditText messageField;
    private ListView chatList;

    // Shared Preferences Declaration
    SharedPreferences sharedPreferences;

    // Database Reference Declaration
    private DatabaseReference databaseReference;

    private String stringMessage;

    // Hardcoded decimal interpretation of keyhex phrase "candydailyisgood"
    private byte encryptionKey[] = {
            99, 97, 110,100, 121, 100, 97, 105, 108, 79, 105, 115, 103, 111, 111, 100
    };

    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use custom Action Bar resource
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();

        /* Home Profile Event Listener */
        mChat = view.findViewById(R.id.chat);
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        MainActivity.this,
                        "You are already on the chats tab",
                        Toast.LENGTH_SHORT).show();

            }
        });

        /* User Profile Event Listener */
        mProfile = view.findViewById(R.id.profile);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        MainActivity.this,
                        ProfileActivity.class
                ));

            }
        });

        // User Settings Event Listener
        mSettings = view.findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        MainActivity.this,
                        SettingsActivity.class
                ));
            }
        });

        // Initialization of Views
        messageField = findViewById(R.id.editText);
        chatList = findViewById(R.id.listView);


        // Initialization of Shared Preferences (username)
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);

        // Retrieve username's key and default value pair (key, value)
        final String username = sharedPreferences.getString("USERNAME", "");

        try {
            // Message represents the message that the end user is sending
            databaseReference = FirebaseDatabase.getInstance().getReference("Message");

            try {
                // AES will be used for encryption and decrypting
                cipher = Cipher.getInstance("AES");
                decipher = Cipher.getInstance("AES");
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                e.printStackTrace();
            }

            // Create a sec
            secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

            databaseReference.addValueEventListener(new ValueEventListener() {

                /**
                 * This method checks to see if there was any data change to the database, this
                 * includes a message being added, or deleted.
                 *
                 * If it does detect a change, it will use the snap shot of the data and update the
                 * UI.  ALl messages are decrypted here.  Note we are decrypting a snapshot not the
                 * actual message stored in the database.
                 *
                 * By doing it this way, the integrity of the message remains intact.  If I was to
                 * decrypt the message in the database, it would be pointless, as the message would
                 * then be in plaintext form within the datbase.
                 * @param dataSnapshot
                 */
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String[] stringKeyValue;
                    // Convert to string
                    stringMessage = dataSnapshot.getValue().toString();
                    // Create substring
                    stringMessage = stringMessage.substring(1, stringMessage.length() - 1);

                    // Split
                    String[] stringMessageArray = stringMessage.split(", ");

                    // Sort
                    Arrays.sort(stringMessageArray);
                    String[] stringFinal = new String[stringMessageArray.length * 2];

                    /* Iterate throught the database and grab all the messages, timestamps, and
                     the author */
                    try {
                        for (int i = 0; i < stringMessageArray.length; i++) {
                            stringKeyValue = stringMessageArray[i].split("=", 2);

                            // Continuing from the split method, extract the time stamp, author
                            stringFinal[2 * i] = "At "
                                    + (String) android.text.format.DateFormat.format(
                                            "dd-MM-yyyy hh:mm:ss",
                                    Long.parseLong(stringKeyValue[0]))+ " " + username + "said";

                            // Retrieve the decoded message
                            stringFinal[2 * i + 1] = AESDecrypt(stringKeyValue[1]);
                        }

                        // Update the List View "chatList" with the messges
                        chatList.setAdapter(new ArrayAdapter<String>(
                                MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                stringFinal
                        ));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** sendButton
     * This method simply sends the message as long as the field is not empty.
     * @param view
     */
    public void sendButton(View view) {

        // Retrieve username's key and default value pair (key, value)
        String username = sharedPreferences.getString("USERNAME", "");

        // Check if message field is empty ...
        if (TextUtils.isEmpty(messageField.getText().toString())) {
            // Text empty
            Toast.makeText(
                    MainActivity.this,
                    "Message cannot be empty"
                    , Toast.LENGTH_SHORT).show();

            // If the message field is not empty, send the message
        } else {
            // Retrieve the date
            Date date = new Date();

            // Send the data to the database, timestamp, and message
            databaseReference.child(Long.toString(date.getTime())).setValue(AESEncrypt(
                    messageField.getText().toString()));

            // Set the input field to empty
            messageField.setText("");
        }


    }

    /** AESEncrypt
     * This method uses an AES Encryption implementation to encrypt the data
     * @param string
     * @return ciphertext
     */
    private String AESEncrypt(String string) {
        // Convert the string to bytes
        byte[] stringByte = string.getBytes();

        // Encrypted byte array initialization
        byte[] encryptedByte = new byte[stringByte.length];

        // Encrypt Implementation
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String ciphertext = null;

        try {
            ciphertext = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ciphertext;
    }

    /** AESDecrypt
     * This method does the inverse of AESEncrypt, and decrypts the message
     * @param string
     * @return plaintext
     * @throws UnsupportedEncodingException
     */
    private String AESDecrypt(String string) throws UnsupportedEncodingException {
        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String plaintext = string;

        byte[] decrypt;

        // Decrypt implementation
        try {
            decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
            decrypt = decipher.doFinal(EncryptedByte);
            plaintext = new String(decrypt);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

}
