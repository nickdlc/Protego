package com.example.protego;


import android.content.Intent;
import android.os.Bundle;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class PatientProfileActivity extends AppCompatActivity {
    public static final String TAG = "PatientProfileActivity";

    // input fields here
    private Button button;
    private Button btnEditProfile;
    private TextView tvPatientFirstName;
    private TextView tvPatientLastName;
    private TextView tvPatientEmail;
    private TextView tvPatientAddress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        mAuth = FirebaseAuth.getInstance();

        //Connects the Edit Profile Code button to the Edit Profile activity
        connectButtonToActivity(R.id.PatientProfileEditButton, PatientEditProfileActivity.class);
        //Connects the Return from Profile button to the Doctor Dashboard Activity
        connectButtonToActivity(R.id.PatientProfileReturnButton, PatientDashboardActivity.class);

        tvPatientFirstName = findViewById(R.id.tvPatientProfileFirstName);
        tvPatientLastName = findViewById(R.id.tvPatientProfileLastName);
        tvPatientEmail = findViewById(R.id.tvPatientProfileEmail);
        tvPatientAddress = findViewById(R.id.tvPatientProfileAddress);

        String puid = mAuth.getCurrentUser().getUid();

        FirestoreAPI.getInstance().getPatient(puid, new FirestoreListener<Patient>() {
            @Override
            public void getResult(Patient object) {
                tvPatientFirstName.setText(object.getFirstName());
                tvPatientLastName.setText(object.getLastName());
                tvPatientEmail.setText(object.getEmail());
                // TODO: Add home address to patients?
                // tvPatientAddress.setText(object.getAddress());
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get patient...\n\t" + msg, e);
            }
        });

        btnEditProfile = findViewById(R.id.PatientProfileEditButton);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PatientEditProfileActivity.class);
                // Put extras to avoid extra Firestore calls in the edit profile activity
                i.putExtra("firstName", tvPatientFirstName.getText().toString());
                i.putExtra("lastName", tvPatientLastName.getText().toString());
                i.putExtra("email", tvPatientEmail.getText().toString());
                // i.putExtra("address", tvPatientAddress.getText().toString());
                startActivity(i);
                finish();
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