package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientMedicationActivity extends AppCompatActivity {
    public static ArrayList<MedicationInfo> medicationData = new ArrayList<>();
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
                createMedication(mAuth.getUid());
                medicationData.clear();
                getPatientMedications(mAuth.getUid());
                recreate();
                Intent i = new Intent(v.getContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void getPatientMedications(String puid) {
        ServerAPI.getMedications(puid, new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                try {
                    JSONArray res = req.getResultJSONList();

                    String name;
                    String datePrescribed;
                    String dosage;
                    String prescriber;


                    for(int i = 0; i < res.length(); i++) {

                        JSONObject object = res.getJSONObject(i);

                        name = object.getString("name");
                        datePrescribed = object.getString("datePrescribed");
                        dosage = object.getString("dosage");
                        prescriber = object.getString("prescriber");

                        medicationData.add(new MedicationInfo(name,datePrescribed,dosage,prescriber));
                        Log.v(TAG, "object: " + object.toString());

                    }

                    Log.v(TAG, "medicationData: " + medicationData.get(0).name);


                } catch (JSONException e) {
                    Log.e(TAG, "Could not get JSON from request : ", e);
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
            }



        });
    }

    private void createMedication(String uid) {

        ServerAPI.generateMedicationData(uid, new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                // do nothing, just generate data
            }

            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }
}
