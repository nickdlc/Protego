package com.example.protego;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SignupActivity {
    public static final String TAG = "SignupActivity";

    // add other input fields here
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        /*
        btnSignup = findViewById(R.id.btnSignup);
            btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");

                String username = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                registerUser(username, password);
            }
        });*/
    }

    private void registerUser(String username, String password) {
        Log.i(TAG, "Attempting to register user " + username);

        // ADD firebase signup here (Parse signup setup is shown)

        /*
        // Create the User
        ParseUser user = new ParseUser();

        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with registration", e);
                    Toast.makeText(LoginActivity.this, "Issue with registration!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                goMainActivity();
            }
        });
        */
    }
}


