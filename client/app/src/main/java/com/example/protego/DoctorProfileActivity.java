package com.example.protego;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.protego.databinding.ActivityDoctorProfileBinding;

public class DoctorProfileActivity extends AppCompatActivity {

    // input fields here
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        //Connects the Edit Profile Code button to the Edit Profile activity
        connectButtonToActivity(R.id.DoctorProfileEditButton, DoctorEditProfileActivity.class);
        //Connects the Return from Profile button to the Doctor Dashboard Activity
        connectButtonToActivity(R.id.DoctorProfileReturnButton, DoctorDashboardActivity.class);

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