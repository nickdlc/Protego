package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.util.RandomGenerator;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.Medication;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientMedicationActivity extends AppCompatActivity {
    public static ArrayList<MedicationInfo> medicationData = new ArrayList<>();
//    public static ArrayList<MedicationInfo> medicationFormatted = new ArrayList<>();
    public static final String TAG = "PatientMedicationActivity";
    private FirebaseAuth mAuth;


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

        mAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

        //setUpMedicationInfo();

        PatientMedicationRecyclerViewAdapter adapter = new PatientMedicationRecyclerViewAdapter(this,medicationData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        findViewById(R.id.add_medication).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicationData.clear();
                createMedication(mAuth.getUid());
                recreate();
                Intent i = new Intent(v.getContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void getPatientMedications(String puid) {
        FirestoreAPI.getInstance().getMedications(puid, new FirestoreListener<List<Medication>>() {
            @Override
            public void getResult(List<Medication> medications) {
                String name;
                String datePrescribed;
                String dosage;
                String prescriber;


                for(Medication med : medications) {
                    name = med.getName();
                    datePrescribed = med.getDatePrescribed().toString();
                    dosage = med.getDosage();
                    prescriber = med.getPrescriber();

                    medicationData
                            .add(new PatientMedicationActivity.MedicationInfo(name,datePrescribed,dosage,prescriber));
                    Log.v(TAG, "object: " + med.toString());
                }

                Log.v(TAG, "medicationData: " + medicationData.get(0).name);
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get medications for patient:\n\t" + msg, e);
                Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

    private void createMedication(String uid) {
        FirestoreAPI.getInstance().generateMedicationData(uid, RandomGenerator.randomApprovedDoctors, new FirestoreListener() {
            @Override
            public void getResult(Object object) {
                // do nothing, just generate data
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }
}
