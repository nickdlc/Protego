package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PatientViewDoctorProfile extends AppCompatActivity {
    public static final String TAG = "PatientViewDoctorProfile";

    private Button btnReturn;
    private Button btnCancelConnection;
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

        btnCancelConnection = findViewById(R.id.btnCancelConnection);
        btnCancelConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelConnection(mAuth.getCurrentUser().getUid(), extras.getString("duid"));

                // The activity is recreated to give the Firestore update enough time to be reflected
                // on the page. If this is not there, the update is not immediately seen in the
                // doctors list.
                // TODO: Find an alternative solution to this?
                Intent i = new Intent(getApplicationContext(), PatientViewDoctorsActivity.class);
                recreate();
                startActivity(i);
                finish();
            }
        });
    }

    private void cancelConnection(String puid, String duid) {
        FirestoreAPI.getInstance().cancelConnection(puid, duid, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                if (object.isSuccessful()) {
                    Log.d(TAG, "Successfully cancelled connection with doctor " + duid);
                    Toast.makeText(
                            getApplicationContext(),
                            "Successfully cancelled connection with this doctor",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, msg, e);
                Toast.makeText(
                        PatientViewDoctorProfile.this,
                        "An error occurred. Please try again.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
