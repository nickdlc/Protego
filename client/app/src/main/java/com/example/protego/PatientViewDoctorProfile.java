package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class PatientViewDoctorProfile extends AppCompatActivity {
    public static final String TAG = "PatientViewDoctorProfile";

    private Button btnReturn;
    private TextView tvDoctorFullName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_doctor_profile);

        mAuth = FirebaseAuth.getInstance();

        tvDoctorFullName = findViewById(R.id.tvDoctorFullName);

        Bundle extras = getIntent().getExtras();
        String doctorFullName = "test";
        if (extras != null) {
            doctorFullName = "Dr. " + extras.getString("firstName") + " " + extras.getString("lastName");
        }

        tvDoctorFullName.setText(doctorFullName);

        btnReturn = findViewById(R.id.returnFromPatientViewDoctorProfile);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PatientViewDoctorsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
