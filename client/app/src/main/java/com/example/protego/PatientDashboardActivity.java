package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PatientDashboardActivity extends AppCompatActivity{
    public static class PatientInfo {
        private final String title;
        private final String details;

        public PatientInfo(String title,String details) {
            this.title = title;
            this.details = details;
        }
    }

    PatientInfo[] patientData = new PatientInfo[] {
            new PatientInfo("Heart Rate:", "Fast"),
            new PatientInfo("Blood Pressure:", "Yes"),
            new PatientInfo("Temperature:", "Maybe")};



    private void getPatientInfo(){
        LinearLayout patientDataLayout =(LinearLayout)findViewById(R.id.patientDataLinearLayout);

        for(PatientInfo info: patientData){
            TextView dataTitle = new TextView(getBaseContext());
            dataTitle.setText(info.title);
            TextView dataDetails = new TextView(getBaseContext());
            dataTitle.setText(info.details);

            LinearLayout newLayout = new LinearLayout(getBaseContext());
            newLayout.setOrientation(LinearLayout.HORIZONTAL);
            newLayout.addView(dataTitle);
            newLayout.addView(dataDetails);

            patientDataLayout.addView(newLayout);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);

        getPatientInfo();
    }
}
