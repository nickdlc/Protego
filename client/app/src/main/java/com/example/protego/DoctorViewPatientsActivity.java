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

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.FirestoreListenerStream;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.Patient;
import com.example.protego.web.schemas.PatientDetails;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewPatientsActivity extends AppCompatActivity {
    public static final String TAG = "DoctorViewPatientActivity";

    // input fields here
    private Button button;
    private List<PatientDetails> patients;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patients);

        //Connects the button to return from the View Patients Activity to the Doctor Dashboard activity
        connectButtonToActivity(R.id.DoctorViewPatientsReturnButton, DoctorDashboardActivity.class);
        //connectButtonToActivity(R.id.testButton, DoctorViewPatientSelections.class);

        mAuth = FirebaseAuth.getInstance();
        DoctorViewPatientsActivity thisObj = this;

        patients = new ArrayList<>();

        //getPatients();

        FirestoreAPI.getInstance().getDoctorAssignedPatients(mAuth.getCurrentUser().getUid(), new FirestoreListener<List<Patient>>() {
            @Override
            public void getResult(List<Patient> pList) {
                System.out.println("Patient List : " + pList);

                RecyclerView rvPatients = findViewById(R.id.rvPatients);
                patients.addAll(PatientDetails.constructPatients(pList));

                // create adapter
                final PatientsListAdapter patientsAdapter = new PatientsListAdapter(thisObj,  patients);
                // Set the adapter on recyclerview
                rvPatients.setAdapter(patientsAdapter);
                // set a layout manager on RV
                rvPatients.setLayoutManager(new LinearLayoutManager(thisObj));
                Log.d(TAG, "Received request for doctor's patients");
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get doctor assigned patients:\n\t" + msg, e);
                Toast.makeText(DoctorViewPatientsActivity.this, msg, Toast.LENGTH_LONG);
            }
        });





    }
/*
    private void getPatients() {

    }*/

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