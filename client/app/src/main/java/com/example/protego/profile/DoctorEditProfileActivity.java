package com.example.protego.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.protego.R;
import com.example.protego.web.firestore.FirestoreAPI;
import com.example.protego.web.firestore.FirestoreListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class DoctorEditProfileActivity extends AppCompatActivity {
    public static final String TAG = "DoctorEditProfileActivity";

    // input fields here
    private Button button;
    private Button btnUpdate;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etWorkplaceName;
    private EditText etAddress;
    private EditText etSpecialty;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);

        mAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        String firstName = extras.getString("firstName");
        String lastName = extras.getString("lastName");
        String email = extras.getString("email");
        String workplaceName = extras.getString("workplaceName");
        String address = extras.getString("address");
        String specialty = extras.getString("specialty");

        etFirstName = findViewById(R.id.etDoctorProfileEditFirstName);
        etLastName = findViewById(R.id.etDoctorProfileEditLastName);
        etEmail = findViewById(R.id.etDoctorProfileEditEmail);
        etWorkplaceName = findViewById(R.id.etDoctorProfileEditWorkplaceName);
        etAddress = findViewById(R.id.etDoctorProfileEditAddress);
        etSpecialty = findViewById(R.id.etDoctorProfileEditSpecialty);

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etEmail.setText(email);
        etWorkplaceName.setText(workplaceName);
        etAddress.setText(address);
        etSpecialty.setText(specialty);

        btnUpdate = findViewById(R.id.DoctorEditProfileUpdateButton);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        //connects the Cancel button to Edit Profile button to the Doctor Profile Activity
        connectButtonToActivity(R.id.DoctorEditProfileReturnButton, DoctorProfileActivity.class);
    }

    private void updateProfile() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String workplaceName = etWorkplaceName.getText().toString();
        String address = etAddress.getText().toString();
        String specialty = etSpecialty.getText().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || workplaceName.isEmpty() ||
            address.isEmpty() || specialty.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    "At least one of the required fields is empty! Please complete all required fields.",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            // Map to update data in Firestore
            Map<String, Object> data = new HashMap<>();

            // Do not put email yet since we only want the update to go through if it is valid
            data.put("firstName", firstName);
            data.put("lastName", lastName);
            data.put("workplaceName", workplaceName);
            data.put("address", address);
            data.put("specialty", specialty);

            if (!user.getEmail().equals(email)) {
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Successfully updated auth email for user " + user.getUid());
                        user.sendEmailVerification();
                        Toast.makeText(
                                getApplicationContext(),
                                "Please check your email, " + email + ", to re-verify your account.",
                                Toast.LENGTH_LONG
                        ).show();

                        // Put email in data map since it is valid
                        data.put("email", email);
                        // Update all fields since the email has changed
                        updateUser(user, data);
                        Toast.makeText(
                                getApplicationContext(),
                                "Successfully updated your profile information.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to update auth email for user " + user.getUid(), e);
                        Toast.makeText(
                                getApplicationContext(),
                                "Failed to update your email: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            } else {
                // Update all fields except for email
                updateUser(user, data);
                Toast.makeText(
                        getApplicationContext(),
                        "Successfully updated your profile information.",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    private void updateUser(FirebaseUser user, Map<String, Object> data) {
        FirestoreAPI.getInstance().updateUser(user, data, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                // Do nothing
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(
                        getApplicationContext(),
                        msg,
                        Toast.LENGTH_LONG
                ).show();
                Log.e(TAG, msg, e);
            }
        });
    }

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();
            }
        });
    }

}