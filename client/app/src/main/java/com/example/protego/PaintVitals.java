package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class PaintVitals extends AppCompatActivity {
    private Button button;

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

    ArrayList<VitalsInfo> patientData = new ArrayList<>();

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_vitals);

        RecyclerView recyclerView = findViewById(R.id.patientVitalsRecyclerView);

        setUpPatientInfo();

        PatientVitalsRecyclerViewAdapter adapter = new PatientVitalsRecyclerViewAdapter(this,patientData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Connects the Edit Profile Code button to the Edit Profile activity
        connectButtonToActivity(R.id.btnReturn, PatientDashboardActivity.class);

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
}

