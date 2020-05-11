package com.example.gsemchat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Author: Louis T. Kavouras
 * Course: MSCS 630 Security Algorithms
 * File: ProfileActivity.java
 * Version 3.0
 * Description:
 * This class is the profile class, which allows users to set a screen name or use their real name.
 * The name will be attached to the messages and is optional for anonymity reasons
 */
public class ProfileActivity extends AppCompatActivity {

    // Shared Preferences Declaration
    SharedPreferences sharedPreferences;

    // Declare views
    ImageView mChat, mProfile, mSettings;
    TextView test;
    EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Use custom Action Bar resource
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();

        /* Home Event Listener */
        mChat = view.findViewById(R.id.chat);
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        ProfileActivity.this,
                        MainActivity.class
                ));

            }
        });

        /* User Profile Event Listener */
        mProfile = view.findViewById(R.id.profile);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        ProfileActivity.this,
                        "You are already on the profile tab",
                        Toast.LENGTH_SHORT).show();

            }
        });

        /* User Settings Event Listener */
        mSettings = view.findViewById(R.id.settings);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        ProfileActivity.this,
                        SettingsActivity.class
                ));
            }
        });

        // Initialization of Shared Preferences (username)
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);

        // Retrieve username's key and default value pair (key, value)
        String username = sharedPreferences.getString("USERNAME", "");

        usernameInput = (EditText) findViewById(R.id.usernameEt);

        // Set Values
        ((EditText) findViewById(R.id.usernameEt)).setText(username);





    } // End of onCreate method

    /** saveUsername()
     * This method simply saves the username, which will be used to show who a message is from
     * @param view
     */
    public void saveUsername(View view) {
        Toast.makeText(this, "Saving!  Please wait...", Toast.LENGTH_SHORT).show();
        // Get input
        String name = usernameInput.getText().toString();

        // Save Username
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", name);
        editor.apply();

        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}
