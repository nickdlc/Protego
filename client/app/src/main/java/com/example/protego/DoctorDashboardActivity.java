package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class DoctorDashboardActivity extends AppCompatActivity{
    //input fields
    private Button button;
    //to store the doctor's last name
    private static String lastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        // update the user's last name based on their profile information
        //TODO: update this last name according to the database
        lastName = "Example";

        //Connects the Scan QR Code button to the QR Code activity
        connectButtonToActivity(R.id.DoctorViewPatientsButton, DoctorViewPatientsActivity.class);
        //Connects the View Patients button to the View Patients Activity
        connectButtonToActivity(R.id.DoctorScanQRCodeButton, DoctorScanQRCodeActivity.class);

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

    //function to call from the navbar fragment class to update the navbar according to the doctor last name
    public static String getName(){
        return lastName;
    }
}

