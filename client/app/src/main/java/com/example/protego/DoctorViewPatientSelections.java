package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoctorViewPatientSelections extends AppCompatActivity {
    // input fields here
    private Button button;
    private TextView tvPatientName;
    private String patientFirst;
    private String patientLast;
    private String name;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_selections);
        connectButtonToActivity(R.id.returnButton, DoctorViewPatientsActivity.class);
        connectButtonToActivity(R.id.generalButton, DoctorViewPatientProfile.class);
        connectButtonToActivity(R.id.medicationButton, DoctorViewPatientMedication.class);
        connectButtonToActivity(R.id.notesButton, DoctorViewPatientNotes.class);
        connectButtonToActivity(R.id.vitalsButton, DoctorViewPatientVitals.class);
        Bundle extras = getIntent().getExtras();
        patientFirst = extras.getString("patientFirst");
        patientLast = extras.getString("patientLast");
        name = patientFirst + " " + patientLast;
        pid = extras.getString("patientId");
        //patientName += extras.get("patientLast");

        tvPatientName = findViewById(R.id.selectionsPatientFullNameInput);
        tvPatientName.setText(name);
    }


    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                i.putExtra("patientFirst", patientFirst);
                i.putExtra("patientLast", patientLast);
                i.putExtra("patientId", pid);
                startActivity(i);
                finish();
            }
        });
    }

}