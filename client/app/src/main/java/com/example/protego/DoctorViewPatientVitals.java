package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DoctorViewPatientVitals extends AppCompatActivity {

    public static ArrayList<PatientVitals.VitalsInfo> patientData = new ArrayList<>();
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_vitals);

        connectButtonToActivity(R.id.returnFromVitals, DoctorViewPatientSelections.class);

        RecyclerView recyclerView = findViewById(R.id.DoctorViewPatientVitalsRecyclerView);


        setUpPatientInfo();

        PatientVitalsRecyclerViewAdapter adapter = new PatientVitalsRecyclerViewAdapter(this,patientData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    private void setUpPatientInfo() {

        patientData.clear();
        patientData.add(new PatientVitals.VitalsInfo("01/01/2022", "Dr. A", "85 bpm", "120/80", "15", "98 F"));
        patientData.add(new PatientVitals.VitalsInfo("01/02/2022", "Dr. B", "86 bpm", "120/80", "15", "98 F"));
        patientData.add(new PatientVitals.VitalsInfo("01/03/2022", "Dr. C", "87 bpm", "120/80", "15", "98 F"));
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
