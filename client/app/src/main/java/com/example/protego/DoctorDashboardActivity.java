package com.example.protego;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Doctor;
import com.example.protego.web.schemas.DoctorDetails;
import com.example.protego.web.schemas.NotificationType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorDashboardActivity extends AppCompatActivity{
    public static final String TAG = "DoctorActivity";

    //input fields
    private Button button;
    private Button scanBtn;
    private DoctorDetails doctorInfo;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //to store the doctor's last name
    public static String lastName;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d(TAG, "Cancelled scan");
                        Toast.makeText(DoctorDashboardActivity.this,
                                "Cancelled the scan",
                                Toast.LENGTH_LONG).show();
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d(TAG, "Cancelled scan due to missing camera permissions");
                        Toast.makeText(DoctorDashboardActivity.this,
                                "Cancelled due to missing camera permissions",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Verify that the result of the scan is a valid patient ID string
                    DocumentReference ref = db.collection("users").document(result.getContents());
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Successfully queried Firestore so check if the result of the
                                // scan is a patient ID that exists
                                DocumentSnapshot doc = task.getResult();

                                if (doc.exists()) {
                                    Log.d(TAG, "Result of QR scan is a valid id");

                                    // Create an active ConnectionRequest between the patient and
                                    // doctor denoted by puid and duid
                                    String duid = mAuth.getCurrentUser().getUid();
                                    String puid = result.getContents();
                                    createConnectionRequest(puid, duid);
                                } else {
                                    Log.d(TAG, "Result of QR scan is an invalid id!");
                                    Toast.makeText(DoctorDashboardActivity.this,
                                            "Scan unsuccessful. Please scan a valid patient's QR code",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.d(TAG, "Failed to query Firestore: ", task.getException());
                                Toast.makeText(DoctorDashboardActivity.this,
                                        "There was an issue finding this patient. Please try again!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

    private void createConnectionRequest(String puid, String duid) {
        // Only create the ConnectionRequest if there is not an existing connection between
        // puid and duid
        db.collection("AssignedTo")
                .whereEqualTo("patient", puid)
                .whereEqualTo("doctor", duid)
                .whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // task.getResult() is the list of all AssignedTo documents with duid and puid
                        if (task.getResult().isEmpty()) {
                            Log.d(TAG, "Connection between " + duid + puid + " does not exist. Now creating ConnectionRequest.");

                            FieldValue timestamp = FieldValue.serverTimestamp();
                            Map<String, Object> crData = new HashMap<>();
                            crData.put("duid", duid);
                            crData.put("puid", puid);
                            crData.put("active", true);
                            crData.put("timeCreated", timestamp);

                            db.collection("ConnectionRequest")
                                    .add(crData)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "Successfully added request to Firestore with id "
                                                    + documentReference.getId());
                                            Log.d(TAG, "Scan successful");
                                            Toast.makeText(DoctorDashboardActivity.this,
                                                    "Scan successful. The patient has received your connection request.",
                                                    Toast.LENGTH_LONG).show();

                                            // Create Notification for patient based on the ConnectionRequest
                                            createNotification(puid, duid, timestamp);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error adding request", e);
                                            Toast.makeText(DoctorDashboardActivity.this,
                                                    "Scan unsuccessful. There was an issue linking to the patient.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Connection between " + duid + puid + " already exists. Skipping ConnectionRequest creation.");
                            Toast.makeText(DoctorDashboardActivity.this,
                                    "You are already connected to this patient!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createNotification(String puid, String duid, FieldValue timestamp) {
        Map<String, Object> nData = new HashMap<>();
        String msg = "You received a connection request from Dr. " + lastName;
        nData.put("puid", puid);
        nData.put("duid", duid);
        nData.put("msg", msg);
        nData.put("timestamp", timestamp);
        nData.put("active", true);
        nData.put("type", NotificationType.CONNECTIONREQUEST.getType());

        db.collection("Notification")
                .add(nData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Added Notification to Firestore with id " +
                                documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding Notification", e);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        doctorInfo = new DoctorDetails();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // update the user's last name based on their profile information
        getDoctorLastName(mAuth.getCurrentUser().getUid());

        //Connects the View Patients button to the View Patients Activity
        connectButtonToActivity(R.id.DoctorViewPatientsButton, DoctorViewPatientsActivity.class);
        //Connects the Scan QR Code button to the QR Code activity
        //connectButtonToActivity(R.id.DoctorScanQRCodeButton, DoctorScanQRCodeActivity.class);

        // Options for QR code scanner
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a patient's QR code.");
        options.setOrientationLocked(false);

        scanBtn = findViewById(R.id.DoctorScanQRCodeButton);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeLauncher.launch(options);
            }
        });

        // manually write in doctor uid for now
        //getDoctorInfo(mAuth.getCurrentUser().getUid());
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

    private void getDoctorLastName(String duid) {
        FirestoreAPI.getInstance().getDoctor(duid, new FirestoreListener<Doctor>() {
            @Override
            public void getResult(Doctor doctor) {
                Log.d(TAG, "req received for doctor : " + doctor);

                if (doctor != null) {
                    doctorInfo.lastName = doctor.getLastName();

                    if (doctorInfo.lastName == null) {
                        lastName = "";
                    } else {
                        lastName = doctorInfo.lastName;
                    }

                    Log.d(TAG, "info last name : " + doctor.getLastName());
                } else {
                    Log.e(TAG, "could not receive doctor info : ");
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "failed to get doctor : " + msg, e);
                Toast.makeText(DoctorDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }


    private void getDoctorInfo(String duid) {
        FirestoreAPI.getInstance().getDoctor(duid, new FirestoreListener<Doctor>() {
            @Override
            public void getResult(Doctor doctor) {
                Log.d(TAG, "req received for doctor : " + doctor);

                if (doctor != null) {
                    doctorInfo.firstName = doctor.getFirstName();
                    doctorInfo.lastName = doctor.getLastName();

                    Log.d(TAG, "info first name : " + doctor.getFirstName());
                    Log.d(TAG, "info last name : " + doctor.getLastName());
                } else {
                    Log.e(TAG, "could not receive doctor info : ");
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "failed to get doctor : " + msg, e);
                Toast.makeText(DoctorDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

    //function to call from the navbar fragment class to update the navbar according to the doctor last name
    public static String getName(){
        return lastName;
    }
}

