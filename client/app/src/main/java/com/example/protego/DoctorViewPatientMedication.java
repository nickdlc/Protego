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
        medicationData.clear();

        medicationData.add(new PatientMedicationActivity.MedicationInfo("1","Medication 1","1/01/2022","x mg","Dr. A"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("2","Medication 2","1/01/2022","x mg","Dr. B"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("3","Medication 3","1/01/2022","x mg","Dr. C"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("4","Medication 4","1/01/2022","x mg","Dr. D"));
        medicationData.add(new PatientMedicationActivity.MedicationInfo("5","Medication 5","1/01/2022","x mg","Dr. E"));
    }



}