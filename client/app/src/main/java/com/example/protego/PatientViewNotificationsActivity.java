package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
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


        String notificationMsg = "No notification message set";
        String type = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notificationMsg = extras.getString("msg");
            type = extras.getString("type");
        }

        setContentView(R.layout.patient_view_notifications_activity);

        notificationText = findViewById(R.id.tvNotificationView);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);
        db = FirebaseFirestore.getInstance();

        notificationText.setText(notificationMsg);

        String duid = extras.getString("duid");
        String puid = extras.getString("puid");

        if(type.equals("ConnectionRequest")) {

            btnAccept.setVisibility(View.VISIBLE);
            btnDecline.setVisibility(View.VISIBLE);

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an active AssignedTo document for the patient and
                    // doctor denoted by patient and doctor
                    FirestoreAPI.getInstance().createConnection(puid, duid, new FirestoreListener<Task>() {
                        @Override
                        public void getResult(Task object) {
                            if (object.isSuccessful()) {
                                // Deactivate corresponding Notification(s) and ConnectionRequest
                                // once the AssignedTo connection is created
                                deactivateNotification(puid, duid);
                                deactivateConnectionRequest(puid, duid);

                                Log.d(TAG, "Successfully created an AssignedTo connection between puid = " +
                                        puid + " and duid = " + duid);
                                Toast.makeText(PatientViewNotificationsActivity.this,
                                        "The connection has been approved and created.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void getError(Exception e, String msg) {
                            Log.e(TAG, msg, e);
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
                            "The connection was not created.",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), PatientDashboardActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
        else{
            btnAccept.setVisibility(View.INVISIBLE);
            btnDecline.setVisibility(View.INVISIBLE);

            deactivateNotification(puid, duid);
        }
    }

    /**
     * @param puid
     * @param duid
     *
     * Deactivates all active ConnectionRequest notifications with puid and duid
     */
    private void deactivateNotification(String puid, String duid) {
        FirestoreAPI.getInstance().deactivateNotification(puid, duid, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                Log.d(TAG, "Successfully deactivated Notification(s) between puid = " + puid +
                        " and duid = " + duid);
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, msg, e);
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
        FirestoreAPI.getInstance().deactivateConnectionRequest(puid, duid, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                if (object.isSuccessful()) {
                    Log.d(TAG, "Successfully deactivated ConnectionRequest between puid = " + puid +
                            " and duid = " + duid);
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, msg, e);
            }
        });
    }
}
