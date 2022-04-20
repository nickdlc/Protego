package com.example.protego;


import android.content.Intent;
import android.os.Bundle;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Doctor;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.protego.databinding.ActivityDoctorProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorProfileActivity extends AppCompatActivity {
    public static final String TAG = "DoctorProfileActivity";

    // input fields here
    private Button button;
    private TextView tvDoctorFullName;
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

        //Connects the Edit Profile Code button to the Edit Profile activity
        connectButtonToActivity(R.id.DoctorProfileEditButton, DoctorEditProfileActivity.class);
        //Connects the Return from Profile button to the Doctor Dashboard Activity
        connectButtonToActivity(R.id.DoctorProfileReturnButton, DoctorDashboardActivity.class);

        tvDoctorFullName = findViewById(R.id.tvDoctorProfileFullName);
        tvDoctorEmail = findViewById(R.id.tvDoctorProfileEmail);
        tvDoctorWorkplaceName = findViewById(R.id.tvDoctorProfileWorkplaceName);
        tvDoctorWorkplaceAddress = findViewById(R.id.tvDoctorProfileAddress);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorProfileSpecialty);

        String duid = mAuth.getCurrentUser().getUid();

        FirestoreAPI.getInstance().getDoctor(duid, new FirestoreListener<Doctor>() {
            @Override
            public void getResult(Doctor object) {
                tvDoctorFullName.setText(object.getFirstName() + " " + object.getLastName());
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