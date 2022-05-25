package com.example.protego.profile;


import android.content.Intent;
import android.os.Bundle;

import com.example.protego.R;
import com.example.protego.dashboard.doctor.DoctorDashboardActivity;
import com.example.protego.web.firestore.FirestoreAPI;
import com.example.protego.web.firestore.FirestoreListener;
import com.example.protego.web.schemas.firestore.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorProfileActivity extends AppCompatActivity {
    public static final String TAG = "DoctorProfileActivity";

    // input fields here
    private Button button;
    private Button btnEditProfile;
    private TextView tvDoctorFirstName;
    private TextView tvDoctorLastName;
    private TextView tvDoctorEmail;
    private TextView tvDoctorWorkplaceName;
    private TextView tvDoctorWorkplaceAddress;
    private TextView tvDoctorSpecialty;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        mAuth = FirebaseAuth.getInstance();

        //Connects the Return from Profile button to the Doctor Dashboard Activity
        connectButtonToActivity(R.id.DoctorProfileReturnButton, DoctorDashboardActivity.class);

        tvDoctorFirstName = findViewById(R.id.tvDoctorProfileFirstName);
        tvDoctorLastName = findViewById(R.id.tvDoctorProfileLastName);
        tvDoctorEmail = findViewById(R.id.tvDoctorProfileEmail);
        tvDoctorWorkplaceName = findViewById(R.id.tvDoctorProfileWorkplaceName);
        tvDoctorWorkplaceAddress = findViewById(R.id.tvDoctorProfileAddress);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorProfileSpecialty);

        String duid = mAuth.getCurrentUser().getUid();

        FirestoreAPI.getInstance().getDoctor(duid, new FirestoreListener<Doctor>() {
            @Override
            public void getResult(Doctor object) {
                tvDoctorFirstName.setText(object.getFirstName());
                tvDoctorLastName.setText(object.getLastName());
                tvDoctorEmail.setText(object.getEmail());
                tvDoctorWorkplaceName.setText(object.getWorkplaceName());
                tvDoctorWorkplaceAddress.setText(object.getAddress());
                tvDoctorSpecialty.setText(object.getSpecialty());
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get doctor...\n\t" + msg, e);
            }
        });

        btnEditProfile = findViewById(R.id.DoctorProfileEditButton);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DoctorEditProfileActivity.class);

                // Put extras to avoid extra Firestore calls in the edit profile activity
                i.putExtra("firstName", tvDoctorFirstName.getText().toString());
                i.putExtra("lastName", tvDoctorLastName.getText().toString());
                i.putExtra("email", tvDoctorEmail.getText().toString());
                i.putExtra("workplaceName", tvDoctorWorkplaceName.getText().toString());
                i.putExtra("address", tvDoctorWorkplaceAddress.getText().toString());
                i.putExtra("specialty", tvDoctorSpecialty.getText().toString());
                Log.d(TAG, "Sending extras via intent");

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