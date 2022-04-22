package com.example.protego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.Notification;
import com.example.protego.web.schemas.NotificationType;
import com.example.protego.web.schemas.PatientDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.protego.web.schemas.MedicalInfo;
import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.Note;
import com.example.protego.web.schemas.Patient;
import com.example.protego.web.schemas.PatientDetails;
import com.example.protego.web.schemas.Vital;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity{
    public static final String TAG = "PatientDashboardActivity";
    //input fields
    private Button button;
    private LinearLayout layout;
    private ImageButton imageButton;
    private Button btnNotifications;
    private TextView tvNotifications;
    private RecyclerView rvNotifications;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PatientDetails patientDetails;
    private ArrayList<Notification> notifications;
    private NotificationListAdapter.RecyclerViewClickListener listener;
    public static String Name;


    public static class PatientInfo {
        private final String title;
        private final String details;

        public PatientInfo(String title,String details) {
            this.title = title;
            this.details = details;
        }

        public String getTitle() {
            return title;
        }

        public String getDetails() {
            return details;
        }
    }

    ArrayList<PatientInfo> patientData = new ArrayList<>();

    private void setUpPatientInfo(){
        patientData.add( new PatientInfo("Heart Rate:", patientDetails.heartRate.toString()));
        patientData.add( new PatientInfo("Blood Pressure:", patientDetails.bloodPressure.toString()));
        // patientData.add( new PatientInfo("Temperature:", "87 Bpm"));
        patientData.add( new PatientInfo("Height (in.)", patientDetails.heightIN.toString()));
        patientData.add( new PatientInfo("Weight (lbs.)", patientDetails.weight.toString()));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);
        btnNotifications = findViewById(R.id.btnNotifications);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        this.patientDetails = new PatientDetails();
        PatientDashboardActivity thisObj = this;

        FirebaseUser currentUser = mAuth.getCurrentUser();

        //updates the navbar to show the patient's first name
        getPatientFirstName(currentUser.getUid());

        PatientVitals.patientData.clear();
        //to get and set the user's vitals for their vitals page
        getPatientVitals(mAuth.getCurrentUser().getUid());

        PatientNotesActivity.notesData.clear();


        //to get and set the user's vitals for their notes page
        getPatientNotes(mAuth.getCurrentUser().getUid());

        PatientMedicationActivity.medicationData.clear();
        //to get and set the user's medication for their medications page
        getPatientMedications(mAuth.getCurrentUser().getUid());

        FirestoreAPI.getInstance().getPatient(currentUser.getUid(), new FirestoreListener<Patient>() {
            @Override
            public void getResult(Patient patient) {
                Log.d(TAG, "req received for patient : " + patient);

                if (patient != null) {
                    patientDetails.firstName = patient.getFirstName();

                    Log.d(TAG, "info first name : " + patient.getFirstName());
                } else {
                    Log.e(TAG, "could not receive patient info : ");
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "failed to get patient : " + msg, e);
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

        FirestoreAPI.getInstance().getMedicalInfo(currentUser.getUid(), new FirestoreListener<MedicalInfo>() {
            @Override
            public void getResult(MedicalInfo medInfo) {
                Log.d(TAG, "req received for medication : " + medInfo);

                if (medInfo != null) {
                    patientDetails.heartRate = medInfo.getHeartRate();
                    patientDetails.bloodPressure = medInfo.getBloodPressure();
                    patientDetails.heightIN = medInfo.getHeightIN();
                    patientDetails.weight = medInfo.getWeight();

                    RecyclerView recyclerView = findViewById(R.id.patientDataRecyclerView);
                    setUpPatientInfo();

                    PatientDashboardRecyclerViewAdapter adapter = new PatientDashboardRecyclerViewAdapter(thisObj, patientData);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));
                } else {
                    Log.e(TAG, "could not receive patient info : ");
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "failed to get medInfo : " + msg, e);
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

        btnNotifications.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeUp() {
                //Toast.makeText(PatientDashboardActivity.this, "Up", Toast.LENGTH_SHORT).show();
                showBottomSheetDialog();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.patientDataRecyclerView);

        // update the user's name based on their profile information
        //TODO: update this name according to the database to get the patient's first name
        Name = "Example";


        //TODO : update the connection, the View Doctors button is connected to the View Notes Activity to test it.
        connectButtonToActivity(R.id.viewDoctorsButton, PatientViewDoctorsActivity.class);
        connectButtonToActivity(R.id.updateDataButton, PatientUpdateDataActivity.class);
        connectImageButtonToActivity(R.id.qrCodeButton, PatientQRCodeDisplay.class);

    }

    public void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.notifications_bottom_sheet);

        LinearLayout menu = bottomSheetDialog.findViewById(R.id.bottom_sheet);

        // Get RecyclerView from bottomSheetDialog
        rvNotifications = bottomSheetDialog.findViewById(R.id.rvNotifications);

        // Populate RecyclerView with patient's notifications
        getNotifications();

        //connects the medication button to the medication activity
        connectLayoutToActivity(R.id.medicationSelectionLayout, PatientMedicationActivity.class,  bottomSheetDialog);
        //connects the notification notes button to the Notes activity
        connectLayoutToActivity(R.id.notesSelectionLayout, PatientNotesActivity.class,  bottomSheetDialog);
        //connects the notification View vitals button to the vitals activity
        connectLayoutToActivity(R.id.VitalsSelectionLayout, PatientVitals.class,  bottomSheetDialog);
        //connects the notification View QR Code button to the View QR Code activity
        connectLayoutToActivity(R.id.viewQRCodeSelectionLayout, PatientQRCodeDisplay.class,  bottomSheetDialog);

        bottomSheetDialog.show();
    }

    private void getNotifications() {
        String puid = mAuth.getCurrentUser().getUid();

        // TODO: limit this to the x most recent notifications
        // Get the active notifications sorted by creation date for patient puid
        FirestoreAPI.getInstance().getNotifications(puid, true, new FirestoreListener<ArrayList<Notification>>() {
            @Override
            public void getResult(ArrayList<Notification> object) {
                notifications = object;
                Collections.sort(notifications, Collections.reverseOrder());
                setRecyclerView();
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, msg, e);
            }
        });
    }

    private void setRecyclerView() {
        // Set the onClick listener for each element in the RecyclerView
        setOnClickListener();

        NotificationListAdapter adapter = new NotificationListAdapter(notifications, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvNotifications.setLayoutManager(layoutManager);
        rvNotifications.setItemAnimator(new DefaultItemAnimator());
        rvNotifications.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new NotificationListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), PatientViewNotificationsActivity.class);
                intent.putExtra("msg", notifications.get(position).getMsg());
                intent.putExtra("duid", notifications.get(position).getDuid());
                intent.putExtra("puid", notifications.get(position).getPuid());
                startActivity(intent);
            }
        };
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

    private void connectImageButtonToActivity(Integer buttonId, Class nextActivityClass) {

        imageButton = findViewById(buttonId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();
            }
        });
    }

    //will connect a layout to an Activity by the layout ID
    private void connectLayoutToActivity(Integer layoutId, Class nextActivityClass, BottomSheetDialog dialog) {
        layout = dialog.findViewById(layoutId);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
            }
        });
        dialog.dismiss();
    }

    private void getPatientFirstName(String puid) {

        FirestoreAPI.getInstance().getPatient(puid, new FirestoreListener<Patient>() {
            @Override
            public void getResult(Patient patient) {
                Log.d(TAG, "req received for patient : " + puid);

                patientDetails.firstName = patient.getFirstName();


                if (patientDetails.firstName == "" || patientDetails.firstName == null) {
                    Name = "";
                } else {
                    Name = patientDetails.firstName;
                }

                Log.d(TAG, "info first name : " + patient.getFirstName());
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get patient:\n\t" + msg, e);
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }
    //function to call from the navbar fragment class to update the navbar according to the patients first name
    public static String getName(){
        return Name;
    }



    private void getPatientVitals(String puid) {
        FirestoreAPI.getInstance().getVitals(puid, new FirestoreListener<List<Vital>>() {
            @Override
            public void getResult(List<Vital> vitals) {
                String heartRate;
                String respiratoryRate;
                String temperature;
                String bloodPressure;
                String source;
                String date;

                for (Vital vital : vitals) {
                    heartRate = String.valueOf(vital.getHeartRate());
                    respiratoryRate = String.valueOf(vital.getRespiratoryRate());
                    temperature = String.valueOf(vital.getTemperature());
                    bloodPressure = String.valueOf(vital.getBloodPressure());
                    source = vital.getSource();
                    date = vital.getDate().toString();

                    PatientVitals.patientData
                            .add(new PatientVitals.VitalsInfo(date,source, heartRate, bloodPressure,respiratoryRate,temperature));

                    Log.v(TAG, "object: " + vitals.toString());

                }
                Log.v(TAG, "patient data index 0: " + PatientVitals.patientData.get(0).getSource());
//                    Log.v(TAG, "patient data index 1: " + PatientVitals.patientData.get(1).getSource());
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }



    private void getPatientNotes(String puid) {
        FirestoreAPI.getInstance().getNotes(puid, new FirestoreListener<List<Note>>() {
            @Override
            public void getResult(List<Note> notes) {
                String title;
                String date;
                String visibility;
                String details;
                String note_id;



                for(Note note : notes) {
                    title = note.getTitle();
                    date = note.getDateCreated().toString();
                    visibility = note.getVisibility();
                    details = note.getContent();
                    note_id = note.getNoteID();

                    PatientNotesActivity.notesData
                            .add(new PatientNotesActivity.NotesInfo(note_id, title,date,visibility,details));

                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get notes for patient:\n\t" + msg, e);
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }



    private void getPatientMedications(String puid) {
        FirestoreAPI.getInstance().getMedications(puid, new FirestoreListener<List<Medication>>() {
            @Override
            public void getResult(List<Medication> medications) {
                String name;
                String datePrescribed;
                String dosage;
                String prescriber;
                String med_id;

                for(Medication med : medications) {
                    name = med.getName();
                    datePrescribed = med.getDatePrescribed().toString();
                    dosage = med.getDosage();
                    prescriber = med.getPrescriber();
                    med_id = med.getMedID();

                    PatientMedicationActivity.medicationData
                            .add(new PatientMedicationActivity.MedicationInfo(med_id,name,datePrescribed,dosage,prescriber));
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get medications for patient:\n\t" + msg, e);
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }


}
