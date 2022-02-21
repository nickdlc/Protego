package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

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
            new PatientInfo("Heart Rate:", "87 Bpm"),
            new PatientInfo("Blood Pressure:", "120.5"),
            new PatientInfo("Temperature:", "97.8"),
            new PatientInfo("Blood-Type:","O-")};



    private void getPatientInfo(){
        LinearLayout patientDataLayout =(LinearLayout)findViewById(R.id.patientDataLinearLayout);

        for(PatientInfo info: patientData){
            TextView dataTitle = new TextView(getBaseContext());
            dataTitle.setText(info.title);
            dataTitle.setTextSize(28);
            dataTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));


            TextView dataDetails = new TextView(getBaseContext());
            dataDetails.setText(info.details);
            dataDetails.setTextSize(28);
            dataDetails.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
            dataDetails.setTypeface(null, android.graphics.Typeface.BOLD);
            dataDetails.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

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
