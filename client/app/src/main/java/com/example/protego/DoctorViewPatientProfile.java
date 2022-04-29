package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.protego.web.schemas.PatientDetails;

import java.util.ArrayList;

public class DoctorViewPatientProfile extends AppCompatActivity {

    private PatientDetails patientDetails;
    //input fields
    private Button button;
    private TextView tvPatientName;
    private String patientFirst;
    private String patientLast;
    private String name;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_profile);

        RecyclerView recyclerView = findViewById(R.id.doctorViewPatientDataRecyclerView);
        setUpPatientInfo();

        connectButtonToActivity(R.id.returnFromGeneral, DoctorViewPatientSelections.class);

        PatientDashboardRecyclerViewAdapter adapter = new PatientDashboardRecyclerViewAdapter(this, patientData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        patientFirst = extras.getString("patientFirst");
        patientLast = extras.getString("patientLast");
        name = patientFirst + " " + patientLast;
        pid = extras.getString("patientId");

        tvPatientName = findViewById(R.id.generalPatientFullNameInput);
        tvPatientName.setText(name);
    }

    ArrayList<PatientDashboardActivity.PatientInfo> patientData = new ArrayList<>();

    private void setUpPatientInfo(){
        patientData.clear();
        patientData.add(new PatientDashboardActivity.PatientInfo("Heart Rate:", "85 bpm"));
        patientData.add(new PatientDashboardActivity.PatientInfo("Blood Pressure:", "110/70"));
        patientData.add(new PatientDashboardActivity.PatientInfo("Height (in.)", "5 ft"));
        patientData.add(new PatientDashboardActivity.PatientInfo("Weight (lbs.)", "140 lbs"));

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
                startActivity(i);
                finish();
            }
        });
    }


}