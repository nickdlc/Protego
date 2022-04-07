package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class PatientViewNotificationsActivity extends AppCompatActivity {
    public static final String TAG = "PatientViewNotificationActivity";

    private FirebaseFirestore db;
    private TextView notificationText;
    private Button btnAccept;
    private Button btnDecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_view_notifications_activity);

        notificationText = findViewById(R.id.tvNotificationView);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);
        db = FirebaseFirestore.getInstance();

        String notificationMsg = "No notification message set";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notificationMsg = extras.getString("msg");
        }

        notificationText.setText(notificationMsg);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an active AssignedTo document for the patient and
                // doctor denoted by patient and doctor
                String duid = extras.getString("duid");
                String puid = extras.getString("puid");
                Map<String, Object> assignedTo = new HashMap<>();
                assignedTo.put("active", true);
                assignedTo.put("doctor", duid);
                assignedTo.put("patient", puid);

                db.collection("AssignedTo")
                    .add(assignedTo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Successfully added assignment to Firestore with id "
                                    + documentReference.getId());
                            Toast.makeText(PatientViewNotificationsActivity.this,
                                    "The connection has been approved and created.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                Intent i = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PatientViewNotificationsActivity.this,
                        "The connection will not be created.",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
