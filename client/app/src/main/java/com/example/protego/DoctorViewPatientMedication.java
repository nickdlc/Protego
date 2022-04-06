package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DoctorViewPatientMedication extends AppCompatActivity {

    private Button button;
    public static ArrayList<PatientMedicationActivity.MedicationInfo> medicationData = PatientMedicationActivity.medicationData;
    public static final String TAG = "PatientViewPatientMedication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_medication);
        connectButtonToActivity(R.id.returnFromMedications, DoctorViewPatientSelections.class);

        setUpMedicationInfo();
        RecyclerView recyclerView = findViewById(R.id.doctorViewPatientMedicationRecyclerView);
        PatientMedicationRecyclerViewAdapter adapter = new PatientMedicationRecyclerViewAdapter(this,medicationData);
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

    private void setUpMedicationInfo() {
        medicationData.add(new PatientMedicationActivity.MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
    }



}