package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DoctorViewPatientSelections extends AppCompatActivity {
    // input fields here
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_selections);
        connectButtonToActivity(R.id.returnButton, DoctorViewPatientsActivity.class);
        connectButtonToActivity(R.id.generalButton, DoctorViewPatientProfile.class);
        connectButtonToActivity(R.id.medicationButton, DoctorViewPatientMedication.class);
        connectButtonToActivity(R.id.notesButton, DoctorViewPatientNotes.class);
        connectButtonToActivity(R.id.vitalsButton, DoctorViewPatientVitals.class);

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