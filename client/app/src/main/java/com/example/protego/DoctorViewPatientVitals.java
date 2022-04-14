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
import com.example.protego.web.schemas.Note;
import com.example.protego.web.schemas.Vital;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewPatientVitals extends AppCompatActivity {

    public static List<Vital> vitalsData;
    private Button button;
    public static final String TAG = "DoctorViewPatientVitals";
    private TextView tvFullName;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_vitals);
        connectButtonToActivity(R.id.returnFromVitals, DoctorViewPatientSelections.class);

        DoctorViewPatientVitals thisObj = this;
        Bundle extras = getIntent().getExtras();
        pid = extras.getString("pid");
        //System.out.println("Passing through " + pid);

        vitalsData = new ArrayList<>();

        //setUpPatientInfo();

        FirestoreAPI.getInstance().getVitals(pid, new FirestoreListener<List<Vital>>() {
            @Override
            public void getResult(List<Vital> vList) {
                System.out.println("Vitals List : " + vList);

                RecyclerView rvVitalsForDoctors = findViewById(R.id.DoctorViewPatientVitalsRecyclerView);

                tvFullName = findViewById(R.id.vitalsPatientFullNameInput);
                tvFullName.setText(extras.getString("patientFullName"));

                for(Vital vital : vList){
                    vitalsData.add(new Vital(vital.getHeartRate(), vital.getRespiratoryRate(), vital.getTemperature(), vital.getDate(), vital.getBloodPressure(), vital.getSource()));
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
    }

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
