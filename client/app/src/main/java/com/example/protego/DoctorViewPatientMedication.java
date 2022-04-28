package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.Patient;
import com.example.protego.web.schemas.PatientDetails;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewPatientMedication extends AppCompatActivity {

    private Button button;
    public static List<PatientMedicationActivity.MedicationInfo> medicationData;
    public static final String TAG = "DoctorViewPatientMedication";
    private FirebaseAuth mAuth;
    private TextView tvFullName;
    private String pid;
    private String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_medication);
        connectButtonToActivity(R.id.returnFromMedications, DoctorViewPatientSelections.class);
        mAuth = FirebaseAuth.getInstance();
        DoctorViewPatientMedication thisObj = this;
        Bundle extras = getIntent().getExtras();
        patientName = extras.getString("patientFirst");
        pid = extras.getString("patientId");

        //System.out.println("Passing through " + pid);

        medicationData = new ArrayList<>();

        //setUpMedicationInfo();

        FirestoreAPI.getInstance().getMedications(pid, new FirestoreListener<List<Medication>>() {
            @Override
            public void getResult(List<Medication> mList) {
                String med_id;
                String name;
                String datePrescribed;
                String dosage;
                String prescriber;

                System.out.println("Medication List : " + mList);

                RecyclerView rvMedicationsForDoctors = findViewById(R.id.doctorViewPatientMedicationRecyclerView);

                tvFullName = findViewById(R.id.medicationPatientFullNameInput);
                tvFullName.setText(extras.getString("patientFirst"));

                for(Medication med : mList){
                    med_id = med.getMedID();
                    name = med.getName();
                    datePrescribed = med.getDatePrescribed().toString();
                    dosage = med.getDosage();
                    prescriber = med.getPrescriber();

                    medicationData.add(new PatientMedicationActivity.MedicationInfo(med_id, name, datePrescribed, dosage, prescriber));
                    Log.d(TAG, "info : " + med.getName());
                    Log.d(TAG, "info : " + med.getDatePrescribed().toString());
                    Log.d(TAG, "info : " + med.getDosage());
                    Log.d(TAG, "info : " + med.getPrescriber());
                }
                //medicationData.addAll(Medication.constructMedications(mList));

                // create adapter
                final PatientMedicationRecyclerViewAdapter adapter = new PatientMedicationRecyclerViewAdapter(thisObj,medicationData);
                // Set the adapter on recyclerview
                rvMedicationsForDoctors.setAdapter(adapter);
                // set a layout manager on RV
                rvMedicationsForDoctors.setLayoutManager(new LinearLayoutManager(thisObj));
                Log.d(TAG, "Received request for patients' medication data");
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get medications:\n\t" + msg, e);
                Toast.makeText(DoctorViewPatientMedication.this, msg, Toast.LENGTH_LONG);
            }
        });
    }




    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                i.putExtra("patientFirst", patientName);
                i.putExtra("patientId", pid);
                startActivity(i);
                finish();
            }
        });
    }

}