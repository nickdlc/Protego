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
import com.example.protego.web.schemas.AssignedTo;
import com.example.protego.web.schemas.Doctor;
import com.example.protego.web.schemas.DoctorDetails;
import com.example.protego.web.schemas.NotificationType;
import com.example.protego.web.schemas.Patient;
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
                    FirestoreAPI.getInstance().getPatient(result.getContents(), new FirestoreListener<Patient>() {
                        @Override
                        public void getResult(Patient object) {
                            if (object != null) {
                                Log.d(TAG, "Result of QR scan is a valid id");

                                // Create a ConnectionRequest between duid and puid
                                String duid = mAuth.getCurrentUser().getUid();
                                String puid = result.getContents();
                                createConnectionRequest(puid, duid);
                            } else {
                                Log.d(TAG, "Result of QR scan is an invalid id!");
                                Toast.makeText(
                                        DoctorDashboardActivity.this,
                                        "Scan unsuccessful. Please scan a valid patient's QR code",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }

                        @Override
                        public void getError(Exception e, String msg) {
                            Log.d(TAG, "Failed to query Firestore: ", e);
                            Toast.makeText(
                                    DoctorDashboardActivity.this,
                                    "There was an issue finding this patient. Please try again!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
                }
            });

    private void createConnectionRequest(String puid, String duid) {
        // Only create the ConnectionRequest if there is not an existing connection between
        // puid and duid
        FirestoreAPI.getInstance().getConnection(puid, duid, true, new FirestoreListener<AssignedTo>() {
            @Override
            public void getResult(AssignedTo object) {
                if (object == null) {
                    // Create an active ConnectionRequest between the patient and doctor denoted by
                    // puid and duid respectively at timestamp
                    FieldValue timestamp = FieldValue.serverTimestamp();
                    FirestoreAPI.getInstance().createConnectionRequest(puid, duid, timestamp, new FirestoreListener<Task>() {
                        @Override
                        public void getResult(Task object) {
                            if (object.isSuccessful()) {
                                Log.d(TAG, "Successfully added request to Firestore");
                                Log.d(TAG, "Scan successful");
                                Toast.makeText(DoctorDashboardActivity.this,
                                        "Scan successful. The patient has received your connection request.",
                                        Toast.LENGTH_LONG).show();

                                // Create a Notification for the patient based on ConnectionRequest
                                createNotification(puid, duid, timestamp);
                            }
                        }

                        @Override
                        public void getError(Exception e, String msg) {
                            Log.e(TAG, msg, e);
                            Toast.makeText(DoctorDashboardActivity.this,
                                    "Scan unsuccessful. There was an issue linking to the patient.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Log.d(TAG, "Connection between " + duid + puid +
                            " already exists. Skipping ConnectionRequest creation.");
                    Toast.makeText(DoctorDashboardActivity.this,
                            "You are already connected to this patient!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, msg, e);
                Toast.makeText(DoctorDashboardActivity.this,
                        "Scan unsuccessful. There was an issue linking to the patient.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createNotification(String puid, String duid, FieldValue timestamp) {
        // Generate a ConnectionRequest Notification for patient puid from doctor duid at timestamp
        FirestoreAPI.getInstance().createNotification(puid, duid, lastName, timestamp, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                if (object.isSuccessful()) {
                    Log.d(TAG, "Successfully added Notification for patient " + puid +
                            "from Dr. " + lastName + " (" + duid + ")");
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, msg, e);
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

