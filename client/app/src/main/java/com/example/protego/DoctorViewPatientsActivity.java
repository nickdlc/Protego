package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DoctorViewPatientsActivity extends AppCompatActivity {

    // input fields here
    private Button button;

    public static class PatientInfo {
        private final String name;

        public PatientInfo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    ArrayList<PatientInfo> patientData = new ArrayList<>();

    private void setUpPatientInfo(){
        patientData.add( new PatientInfo("Mama, Joe"));
        patientData.add( new PatientInfo("Mama, Joe"));
        patientData.add( new PatientInfo("Mama, Joe"));
        patientData.add( new PatientInfo("Mama, Joe"));
        patientData.add( new PatientInfo("Mama, Joe"));
        patientData.add( new PatientInfo("Mama, Joe"));
        patientData.add( new PatientInfo("Mama, Joe"));
        patientData.add( new PatientInfo("Mama, Joe"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patients);

        //Connects the button to return from the View Patients Activity to the Doctor Dashboard activity
        connectButtonToActivity(R.id.DoctorViewPatientsReturnButton, DoctorDashboardActivity.class);

        // Sets up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.viewPatientsRecyclerView);
        // Adds and gets patient info
        setUpPatientInfo();
        // Connects RecyclerView to adapter
        DoctorViewPatientsRecyclerView adapter = new DoctorViewPatientsRecyclerView(this,patientData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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