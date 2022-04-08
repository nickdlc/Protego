package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.schemas.NotificationType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
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

        String duid = extras.getString("duid");
        String puid = extras.getString("puid");

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an active AssignedTo document for the patient and
                // doctor denoted by patient and doctor
                Map<String, Object> assignedTo = new HashMap<>();
                assignedTo.put("active", true);
                assignedTo.put("doctor", duid);
                assignedTo.put("patient", puid);

                db.collection("AssignedTo")
                    .add(assignedTo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            deactivateNotification(puid, duid);
                            deactivateConnectionRequest(puid, duid);
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
                deactivateNotification(puid, duid);
                deactivateConnectionRequest(puid, duid);
                Toast.makeText(PatientViewNotificationsActivity.this,
                        "The connection will not be created.",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * @param puid
     * @param duid
     *
     * Deactivates all active ConnectionRequest notifications with puid and duid
     */
    private void deactivateNotification(String puid, String duid) {
        db.collection("Notification")
                .whereEqualTo("puid", puid)
                .whereEqualTo("duid", duid)
                .whereEqualTo("type", NotificationType.CONNECTIONREQUEST.getType())
                .whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference dr = db.collection("Notification")
                                        .document(document.getId());

                                batch.update(dr, "active", false);

                                Log.d(TAG, "Added to batch: " + document.getId());
                            }
                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "Committed batch to deactivate notifications");
                                }
                            });
                        }
                    }
                });
    }

    /**
     * @param puid
     * @param duid
     *
     * Deactivates all active ConnectionRequests with puid and duid
     */
    private void deactivateConnectionRequest(String puid, String duid) {
        db.collection("ConnectionRequest")
                .whereEqualTo("puid", puid)
                .whereEqualTo("duid", duid)
                .whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference dr = db.collection("ConnectionRequest")
                                        .document(document.getId());

                                batch.update(dr, "active", false);

                                Log.d(TAG, "Added to batch: " + document.getId());
                            }
                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "Committed batch to deactivate connection requests");
                                }
                            });
                        }
                    }
                });
    }
}
