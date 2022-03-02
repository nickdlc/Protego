package com.example.protego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    public static final String TAG = "ForgotPasswordActivity";
    FirebaseAuth mAuth;

    private EditText etEmail;
    private Button btnResetPassword;
    private Button btnReturnToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick reset password button");

                resetPassword();

                /* TODO: Hide keyboard when reset password button is clicked. */
            }
        });
        btnReturnToSignIn = findViewById(R.id.btnReturnToSignIn);
        btnReturnToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick return to sign in button");

                Intent i = new Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void resetPassword() {
        String email = etEmail.getText().toString().trim();

        // handle egregious email inputs

        if (email.isEmpty()) {
            etEmail.setError("Please enter your email.");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email.");
            etEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Please check your email to reset your password!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Something went wrong. Please check that your email is correct!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}