package com.example.protego;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Doctor;
import com.example.protego.web.schemas.Vital;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewPatientVitals
        extends FragmentActivity
        implements DoctorNewPatientVitalsFragment.NoticeDialogListener {

    public static List<PatientVitals.VitalsInfo> vitalsData;
    private Button button;
    private Button addBtn;
    public static final String TAG = "DoctorViewPatientVitals";
    private TextView tvFullName;
    private SwipeRefreshLayout swipeContainer;
    private FirebaseAuth mAuth;
    private String pid;
    private String patientFirst;
    private String patientLast;
    private String name;
    private String onboardingFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_vitals);

        mAuth = FirebaseAuth.getInstance();
        connectButtonToActivity(R.id.returnFromVitals, DoctorViewPatientSelections.class);

        DoctorViewPatientVitals thisObj = this;
        Bundle extras = getIntent().getExtras();
        patientFirst = extras.getString("patientFirst");
        patientLast = extras.getString("patientLast");
        name = patientFirst + " " + patientLast;
        pid = extras.getString("patientId");
        onboardingFlag = extras.getString("onboardingFlag");

        //System.out.println("Passing through " + pid);

        vitalsData = new ArrayList<>();

        tvFullName = findViewById(R.id.vitalsPatientFullNameInput);
        tvFullName.setText(name);

        addBtn = findViewById(R.id.doctorAddNewVital);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVital();
            }
        });

        FirestoreAPI.getInstance().getVitals(pid, new FirestoreListener<List<Vital>>() {
            @Override
            public void getResult(List<Vital> vList) {
                System.out.println("Vitals List : " + vList);

                int heartRate;
                int respiratoryRate;
                double temperature;
                String puid;
                String bloodPressure;
                String source;
                String date;

                RecyclerView rvVitalsForDoctors = findViewById(R.id.DoctorViewPatientVitalsRecyclerView);

                for(Vital vital : vList){
                    heartRate = vital.getHeartRate();
                    respiratoryRate = vital.getRespiratoryRate();
                    temperature = vital.getTemperature();
                    bloodPressure = vital.getBloodPressure();
                    source = vital.getSource();
                    date = vital.getDate().toString();

                    vitalsData.add(new PatientVitals.VitalsInfo(date, source, heartRate, bloodPressure, respiratoryRate, temperature));
                }

                // create adapter
                final PatientVitalsRecyclerViewAdapter adapter = new PatientVitalsRecyclerViewAdapter(thisObj, vitalsData);
                // Set the adapter on recyclerview
                rvVitalsForDoctors.setAdapter(adapter);
                // set a layout manager on RV
                rvVitalsForDoctors.setLayoutManager(new LinearLayoutManager(thisObj));
                Log.d(TAG, "Received request for patients' vitals data");
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get vitals:\n\t" + msg, e);
                Toast.makeText(DoctorViewPatientVitals.this, msg, Toast.LENGTH_LONG);
            }
        });

        final PatientVitalsRecyclerViewAdapter adapter = new PatientVitalsRecyclerViewAdapter(thisObj, vitalsData);

        // Look up the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.doctorViewPatientVitalsSwipeContainer);

        // Set up refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                FirestoreAPI.getInstance().getVitals(pid, new FirestoreListener<List<Vital>>() {
                    @Override
                    public void getResult(List<Vital> vitals) {
                        int heartRate;
                        int respiratoryRate;
                        double temperature;
                        String puid;
                        String bloodPressure;
                        String source;
                        String date;

                        RecyclerView recyclerView = findViewById(R.id.DoctorViewPatientVitalsRecyclerView);

                        for (Vital vital : vitals) {
                            heartRate = vital.getHeartRate();
                            respiratoryRate = vital.getRespiratoryRate();
                            temperature = vital.getTemperature();
                            bloodPressure = vital.getBloodPressure();
                            source = vital.getSource();
                            date = vital.getDate().toString();

                            vitalsData.add(new PatientVitals.VitalsInfo(date,source, heartRate, bloodPressure, respiratoryRate, temperature));
                        }

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));

                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void getError(Exception e, String msg) {
                        Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                        Toast.makeText(
                                getApplicationContext(),
                                msg + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
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
                i.putExtra("onboardingFlag", onboardingFlag);
                startActivity(i);
                finish();
            }
        });
    }

    private void addVital(){
        DialogFragment fragment = new DoctorNewPatientVitalsFragment();
        fragment.show(getSupportFragmentManager(), TAG + ": addVital");
    }

    private void createVital(String bloodPressure, int heartRate, int respiratoryRate, String doctor, double temperature) {
        FieldValue timestamp = FieldValue.serverTimestamp();
        FirestoreAPI.getInstance().createVital(pid, heartRate, respiratoryRate, temperature, timestamp, bloodPressure, doctor, new FirestoreListener<Task>() {
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
                Toast.makeText(
                        getApplicationContext(),
                        msg + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String bloodPressure = DoctorNewPatientVitalsFragment.vitalBloodPressure;
        String heartRate = DoctorNewPatientVitalsFragment.vitalHeartRate;
        String respiratoryRate = DoctorNewPatientVitalsFragment.vitalRespiratoryRate;
        String temperature = DoctorNewPatientVitalsFragment.vitalTemperature;
        String duid = mAuth.getCurrentUser().getUid();

        if (bloodPressure != null && heartRate != null && respiratoryRate != null && temperature != null) {
            FirestoreAPI.getInstance().getDoctor(duid, new FirestoreListener<Doctor>() {
                @Override
                public void getResult(Doctor object) {
                    Log.d(TAG, "Attempting to create new vitals entry with doctor " + duid + " for patient " + pid);
                    String sourceDoctor = "Dr. " + object.getLastName();
                    createVital(bloodPressure, Integer.parseInt(heartRate), Integer.parseInt(respiratoryRate), sourceDoctor, Double.parseDouble(temperature));
                    dialog.dismiss();
                }

                @Override
                public void getError(Exception e, String msg) {
                    Log.e(TAG, msg, e);
                    Toast.makeText(
                            getApplicationContext(),
                            "Something went wrong when trying to add a new vitals entry. Please try again!",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
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
