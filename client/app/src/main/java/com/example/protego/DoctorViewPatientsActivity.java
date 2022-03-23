package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.PatientDetails;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        mAuth = FirebaseAuth.getInstance();
        getPatients();
    }

    private void getPatients() {
        ServerAPI.getDoctorAssignedPatients(mAuth.getCurrentUser().getUid(), new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                Log.d(TAG, "Received request for doctor's patients");
                try {
                    JSONArray res = req.getResultJSONList();

                    System.out.println("Patient List : " + res.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(DoctorViewPatientsActivity.this, msg, Toast.LENGTH_LONG);
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