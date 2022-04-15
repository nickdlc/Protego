package com.example.protego;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.protego.util.RandomGenerator;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;

import com.example.protego.web.schemas.Vital;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class PatientVitals extends AppCompatActivity {
    private Button button;
    private FirebaseAuth mAuth;
    public static final String TAG = "PatientVitalsActivity";
    public static ArrayList<VitalsInfo> patientData = new ArrayList<>();
    //public static PatientVitalsRecyclerViewAdapter adapter;

    public static class  VitalsInfo {
        private final String date;
        private final String source;
        private final String heartRate;
        private final String bloodPressure;
        private final String respiratoryRate;
        private final String temperature;
        public VitalsInfo(String date, String source, String heartRate, String bloodPressure, String respiratoryRate, String temperature) {
            this.date = date;
            this.source = source;
            this.heartRate = heartRate;
            this.bloodPressure = bloodPressure;
            this.respiratoryRate = respiratoryRate;
            this.temperature = temperature;
        }

        public String getDate() {
            return date;
        }

        public String getSource() {
            return source;
        }

        public String getHeartRate() {
            return heartRate;
        }

        public String getBloodPressure() {
            return bloodPressure;
        }

        public String getRespiratoryRate() {
            return respiratoryRate;
        }

        public String getTemperature() {
            return temperature;
        }

    }


    private void setUpPatientInfo(){


        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));
        patientData.add(new VitalsInfo("01/01/2022","Dr. Seuss","99","99","99","99"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_vitals);

        mAuth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.patientVitalsRecyclerView);

        PatientVitalsRecyclerViewAdapter adapter = new PatientVitalsRecyclerViewAdapter(this,patientData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Connects the Edit Profile Code button to the Edit Profile activity
        connectButtonToActivity(R.id.btnReturn, PatientDashboardActivity.class);


        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createVital(mAuth.getUid());
                patientData.clear();
                Intent i = new Intent(v.getContext(), PatientDashboardActivity.class);
                startActivity(i);

                finish();
            }
        });
    }

    private void getPatientVitals(String puid) {
        FirestoreAPI.getInstance().getVitals(puid, new FirestoreListener<List<Vital>>() {
            @Override
            public void getResult(List<Vital> vitals) {
                String heartRate;
                String respiratoryRate;
                String temperature;
                String puid;
                String bloodPressure;
                String source;
                String date;

                for (Vital vital : vitals) {
                    heartRate = String.valueOf(vital.getHeartRate());
                    respiratoryRate = String.valueOf(vital.getRespiratoryRate());
                    temperature = String.valueOf(vital.getTemperature());
                    bloodPressure = String.valueOf(vital.getBloodPressure());
                    source = vital.getSource();
                    date = vital.getDate().toString();

                    patientData.add(new PatientVitals.VitalsInfo(date,source, heartRate, bloodPressure,respiratoryRate,temperature));
                    Log.v(TAG, "object: " + vitals.toString());

                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                Toast.makeText(PatientVitals.this, msg, Toast.LENGTH_LONG);
            }
        });
    }


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



    private void createVital(String uid){
        FirestoreAPI.getInstance().generateVitalData(uid, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                // do nothing, just generate data
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(PatientVitals.this, msg, Toast.LENGTH_LONG);
            }
        });
    }
}

