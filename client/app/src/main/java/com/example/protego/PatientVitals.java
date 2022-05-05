package com.example.protego;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractCollection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class PatientVitals
        extends FragmentActivity
        implements PatientNewVitalsFragment.NoticeDialogListener {
    private Button button;
    private FirebaseAuth mAuth;
    public static final String TAG = "PatientVitalsActivity";
    public static String userID;
    public static List<VitalsInfo> patientData;
    private SwipeRefreshLayout swipeContainer;
    //public static PatientVitalsRecyclerViewAdapter adapter;

    public static class VitalsInfo {
        private final String date;
        private final String source;
        private final int heartRate;
        private final String bloodPressure;
        private final int respiratoryRate;
        private final double temperature;

        public VitalsInfo(String date, String source, int heartRate, String bloodPressure, int respiratoryRate, double temperature) {
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

        public int getHeartRate() {
            return heartRate;
        }

        public String getBloodPressure() {
            return bloodPressure;
        }

        public int getRespiratoryRate() {
            return respiratoryRate;
        }

        public double getTemperature() {
            return temperature;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_vitals);

        patientData = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        PatientVitals thisObj = this;

        FirestoreAPI.getInstance().getVitals(userID, new FirestoreListener<List<Vital>>() {
            @Override
            public void getResult(List<Vital> vitals) {
                int heartRate;
                int respiratoryRate;
                double temperature;
                String puid;
                String bloodPressure;
                String source;
                String date;

                RecyclerView recyclerView = findViewById(R.id.patientVitalsRecyclerView);

                for (Vital vital : vitals) {
                    heartRate = vital.getHeartRate();
                    respiratoryRate = vital.getRespiratoryRate();
                    temperature = vital.getTemperature();
                    bloodPressure = vital.getBloodPressure();
                    source = vital.getSource();
                    date = vital.getDate().toString();

                    patientData.add(new VitalsInfo(date,source, heartRate, bloodPressure, respiratoryRate, temperature));
                }

                final PatientVitalsRecyclerViewAdapter adapter = new PatientVitalsRecyclerViewAdapter(thisObj,patientData);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                Toast.makeText(PatientVitals.this, msg, Toast.LENGTH_LONG);
            }
        });

        final PatientVitalsRecyclerViewAdapter adapter = new PatientVitalsRecyclerViewAdapter(thisObj, patientData);

        // Look up the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.patientVitalsSwipeContainer);

        // Set up refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                FirestoreAPI.getInstance().getVitals(userID, new FirestoreListener<List<Vital>>() {
                    @Override
                    public void getResult(List<Vital> vitals) {
                        int heartRate;
                        int respiratoryRate;
                        double temperature;
                        String puid;
                        String bloodPressure;
                        String source;
                        String date;

                        RecyclerView recyclerView = findViewById(R.id.patientVitalsRecyclerView);

                        for (Vital vital : vitals) {
                            heartRate = vital.getHeartRate();
                            respiratoryRate = vital.getRespiratoryRate();
                            temperature = vital.getTemperature();
                            bloodPressure = vital.getBloodPressure();
                            source = vital.getSource();
                            date = vital.getDate().toString();

                            patientData.add(new VitalsInfo(date,source, heartRate, bloodPressure, respiratoryRate, temperature));
                        }

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));

                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void getError(Exception e, String msg) {
                        Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    }
                });
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        //Connects the Edit Profile Code button to the Edit Profile activity
        connectButtonToActivity(R.id.btnReturn, PatientDashboardActivity.class);

        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVital();
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

    private void addVital(){
        DialogFragment fragment = new PatientNewVitalsFragment();
        fragment.show(getSupportFragmentManager(), TAG + ": createVital");
    }

    private void createVital(String bloodPressure, int heartRate, int respiratoryRate, String doctor, double temperature) {
        String puid = mAuth.getCurrentUser().getUid();
        FieldValue timestamp = FieldValue.serverTimestamp();
        FirestoreAPI.getInstance().createVital(puid, heartRate, respiratoryRate, temperature, timestamp, bloodPressure, doctor, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                DocumentReference result = (DocumentReference) object.getResult();
                String id = result.getId();
                result.update("vitalID", id);

                Log.d(TAG, "Added vital " + id);
                Toast.makeText(
                        getApplicationContext(),
                        "Successfully added new vitals entry. Please swipe up to refresh.",
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String bloodPressure = PatientNewVitalsFragment.vitalBloodPressure;
        String heartRate = PatientNewVitalsFragment.vitalHeartRate;
        String respiratoryRate = PatientNewVitalsFragment.vitalRespiratoryRate;
        String sourceDoctor = PatientNewVitalsFragment.vitalSource;
        String temperature = PatientNewVitalsFragment.vitalTemperature;

        if (bloodPressure != null && heartRate != null && respiratoryRate != null && sourceDoctor != null && temperature != null) {
            createVital(bloodPressure, Integer.parseInt(heartRate), Integer.parseInt(respiratoryRate), sourceDoctor, Double.parseDouble(temperature));
            dialog.dismiss();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Please complete all fields.",
                    Toast.LENGTH_LONG
            ).show();
            addVital();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Use the default behavior set in PatientNewVitalsFragment
    }
}

