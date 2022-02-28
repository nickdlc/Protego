package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class PatientDashboardActivity extends AppCompatActivity{
    public static class PatientInfo {
        private final String title;
        private final String details;

        public PatientInfo(String title,String details) {
            this.title = title;
            this.details = details;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }
    }

    ArrayList<PatientInfo> patientData = new ArrayList<>();

    private void setUpPatientInfo(){
        patientData.add( new PatientInfo("Heart Rate:", "87 Bpm"));
        patientData.add( new PatientInfo("Blood Pressure:", "87 Bpm"));
        patientData.add( new PatientInfo("Temperature:", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
        patientData.add( new PatientInfo("Blood-Type", "87 Bpm"));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);

        RecyclerView recyclerView = findViewById(R.id.patientDataRecyclerView);

        setUpPatientInfo();

        PatientDashboardRecyclerViewAdapter adapter = new PatientDashboardRecyclerViewAdapter(this,patientData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
