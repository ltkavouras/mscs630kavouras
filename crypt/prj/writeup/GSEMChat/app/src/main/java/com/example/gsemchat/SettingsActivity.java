package com.example.gsemchat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

/**
 * Author: Louis T. Kavouras
 * Course: MSCS 630 Security Algorithms
 * File: ProfileActivity.java
 * Version 2.5
 * Description:
 * This class is used for the end-user's settings.  This tab, allows for the user to create a
 * secret key using a plaintext phrase of 16 characters
 */

public class SettingsActivity extends AppCompatActivity {

    // Shared Preferences Declaration
    SharedPreferences sharedPreferences;

    // Declare views
    ImageView mChat, mProfile, mSettings;
    EditText userPhraseField;
    TextView outputTv;
    Button generateButton;

    String outputString;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                startActivity(new Intent(
                        SettingsActivity.this,
                        ProfileActivity.class
                ));
            }
        });

        /* User Profile Event Listener */
        mProfile = view.findViewById(R.id.profile);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        SettingsActivity.this,
                        MainActivity.class
                ));

            }
        });

        // User Settings Event Listener
        mSettings = view.findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        SettingsActivity.this,
                        "You are already on the settings tab",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Initialize Views
        userPhraseField = (EditText) findViewById(R.id.phraseEt);
        outputTv = (TextView) findViewById(R.id.outputText);
        generateButton = (Button) findViewById(R.id.generateBtn);

        // Initialization of Shared Preferences (username)
        sharedPreferences = getSharedPreferences("PHRASE", MODE_PRIVATE);

        String encryptedPhrase = sharedPreferences.getString("PHRASE", "");

        outputTv.setText(encryptedPhrase);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePhrase();
            }
        });

    }

    // Convert plaintext phrase to hexidecimal based text
    private void generatePhrase() {
        // Display a message to the UI indicating that the encryption key is being created.
        Toast.makeText(
                this,
                "Generating your key!  Please wait...",
                Toast.LENGTH_SHORT).show();

        // Retrieve the plaintext phrase provided by the end-user
        String plaintextPhrase = userPhraseField.getText().toString();

        if (plaintextPhrase.length() < 16) {
            // Send toast indication to UI telling the end-user that the key generating was a success
            Toast.makeText(
                    this,
                    "Sorry, that phrase is too short",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Initialize final String
            String hex = "";

            // Make a loop to iterate through every character of ascii string
            for (int i = 0; i < plaintextPhrase.length(); i++) {

                // take a char from position i of string
                char character = plaintextPhrase.charAt(i);

                // cast char to integer and find its ascii value
                int in = (int)character;

                // change this ascii value integer to hexadecimal value
                String part = Integer.toHexString(in);

                // add this hexadecimal value to final string.
                hex += part;


            }

            // Store plaintext phrase
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("PHRASE", hex);
            editor.apply();

            String encryptedPhrase = sharedPreferences.getString("PHRASE", "");

            outputTv.setText(encryptedPhrase);

            // Display a message to the UI indicating that the encryption key is being created.
            Toast.makeText(this, "Generated Successfully!", Toast.LENGTH_SHORT).show();

        }

    }

}
