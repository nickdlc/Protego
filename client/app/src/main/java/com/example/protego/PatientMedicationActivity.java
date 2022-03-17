package com.example.protego;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientMedicationActivity extends AppCompatActivity {
    public static class MedicationInfo {
        private final String name,date,dosage,prescribedBy;

        public MedicationInfo(String name, String date, String dosage, String prescribedBy) {
            this.name = name;
            this.date = date;
            this.dosage = dosage;
            this.prescribedBy = prescribedBy;
        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public String getDosage() {
            return dosage;
        }

        public String getPrescribedBy() {
            return prescribedBy;
        }
    }

    ArrayList<MedicationInfo> medicationData = new ArrayList<>();

    private void setUpMedicationInfo() {
        medicationData.add(new MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("Jay","1/01/2022","A lot","Dr. M"));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_medication);

        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

        setUpMedicationInfo();

        PatientMedicationRecyclerViewAdapter adapter = new PatientMedicationRecyclerViewAdapter(this,medicationData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
