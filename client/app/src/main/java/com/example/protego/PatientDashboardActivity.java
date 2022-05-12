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
import com.example.protego.web.schemas.Onboarding.AllergyInfo;
import com.example.protego.web.schemas.Onboarding.CancerInfo;
import com.example.protego.web.schemas.Onboarding.DiabetesInfo;
import com.example.protego.web.schemas.Onboarding.OtherConditionsInfo;
import com.example.protego.web.schemas.Onboarding.SurgeryInfo;
import com.example.protego.web.schemas.PatientDetails;
import com.example.protego.web.schemas.ProtegoUser;
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
    private ArrayList<Vital> vitals;
    private NotificationListAdapter.RecyclerViewClickListener listener;
    public static String Name;
    public static String flagData;
    public static String phoneData, heightData, weightData, emergencyPhoneData, emergencyNameData, emergencyEmailData, addressData;
    public static ArrayList<AllergyInfo> allergiesList = new ArrayList<>();
    public static ArrayList<CancerInfo> cancerList = new ArrayList<>();
    public static ArrayList<DiabetesInfo> diabetesList = new ArrayList<>();
    public static ArrayList<SurgeryInfo> surgeryList = new ArrayList<>();
    public static ArrayList<OtherConditionsInfo> otherConditionsList = new ArrayList<>();

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

    // Function that handles going to onboarding if user is new
    private void goToOnboarding(String uid) {

        Log.v(TAG, "onboarding flag: "+ flagData);

        if(PatientOnboardingActivity.flag.equals("false")) {
            Log.v(TAG, "patient onboarding flag: "+ PatientOnboardingActivity.flag);
            Button button = (Button) findViewById(R.id.onBoardingButton);
            button.setVisibility(View.VISIBLE);
        }
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
        RecyclerView recyclerView = findViewById(R.id.patientDataRecyclerView);

        goToOnboarding(currentUser.getUid()); //checks the flag set during onboarding to determine if the onboarding button should be visible

        //updates the navbar to show the patient's first name
        getPatientFirstName(currentUser.getUid());

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

        FirestoreAPI.getInstance().getVitals(currentUser.getUid(), new FirestoreListener<List<Vital>>() {
            @Override
            public void getResult(List<Vital> object) {
                if (!object.isEmpty()) {
                    vitals = new ArrayList<>(object);
                    Collections.sort(vitals, Collections.reverseOrder());
                    // grab the most recent vital
                    Vital v = vitals.get(0);
                    patientData.add(new PatientInfo("Heart Rate:", String.valueOf(v.getHeartRate())));
                    patientData.add(new PatientInfo("Blood Pressure:", v.getBloodPressure()));
                    patientData.add(new PatientInfo("Respiratory Rate:", String.valueOf(v.getRespiratoryRate())));
                    patientData.add(new PatientInfo("Temperature:", String.valueOf(v.getTemperature())));
                } else {
                    patientData.add(new PatientInfo("Heart Rate:", "N/A"));
                    patientData.add(new PatientInfo("Blood Pressure:", "N/A"));
                    patientData.add(new PatientInfo("Respiratory Rate:", "N/A"));
                    patientData.add(new PatientInfo("Temperature:", "N/A"));
                }

                PatientDashboardRecyclerViewAdapter adapter = new PatientDashboardRecyclerViewAdapter(thisObj, patientData);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, msg, e);
                Toast.makeText(
                        getApplicationContext(),
                        msg,
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        btnNotifications.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeUp() {
                //Toast.makeText(PatientDashboardActivity.this, "Up", Toast.LENGTH_SHORT).show();
                showBottomSheetDialog();
            }
        });

        // update the user's name based on their profile information
        Name = "Example";


        connectButtonToActivity(R.id.viewDoctorsButton, PatientViewDoctorsActivity.class);
        // connectButtonToActivity(R.id.updateDataButton, PatientUpdateDataActivity.class);
        connectImageButtonToActivity(R.id.qrCodeButton, PatientQRCodeDisplay.class);
        connectButtonToActivity(R.id.onBoardingButton, PatientOnboardingActivity.class);

        //to get the user's onboarding detail if the form has been completed
        if(PatientOnboardingActivity.flag.equals("true")){
            getOnboardingDetails(currentUser.getUid());
        }

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
        //connects the notification View geneeral Information button to the View QR Code activity
        connectLayoutToActivity(R.id.viewQRCodeSelectionLayout, PatientGeneralInfoActivity.class,  bottomSheetDialog);

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
                intent.putExtra("type", notifications.get(position).getType());
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


                if (patientDetails.firstName.equals("") || patientDetails.firstName == null) {
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
                int heartRate;
                int respiratoryRate;
                double temperature;
                String puid;
                String bloodPressure;
                String source;
                String date;

                for (Vital vital : vitals) {
                    heartRate = vital.getHeartRate();
                    respiratoryRate = vital.getRespiratoryRate();
                    temperature = vital.getTemperature();
                    bloodPressure = vital.getBloodPressure();
                    source = vital.getSource();
                    date = vital.getDate().toString();

                    PatientVitals.patientData
                            .add(new PatientVitals.VitalsInfo(date,source, heartRate, bloodPressure, respiratoryRate, temperature));
                    Log.v(TAG, "object: " + vitals.toString());
                }
                //Log.v(TAG, "patient data index 0: " + PatientVitals.patientData.get(0).getSource());
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
                String frequency;

                for(Medication med : medications) {
                    name = med.getName();
                    datePrescribed = med.getDatePrescribed().toString();
                    dosage = med.getDosage();
                    prescriber = med.getPrescriber();
                    med_id = med.getMedID();
                    frequency = med.getFrequency();

                    PatientMedicationActivity.medicationData
                            .add(new PatientMedicationActivity.MedicationInfo(
                                    med_id, name, datePrescribed, dosage, prescriber, frequency
                            ));
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get medications for patient:\n\t" + msg, e);
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

    //to retrieve the onboarding fields from the dashboard to display immediately on the general health Info page
    private void getOnboardingDetails(String id){

        FirestoreAPI.getInstance().getOnboardingDetails(id, new FirestoreListener<DocumentSnapshot>() {
            @Override
            public void getResult(DocumentSnapshot object) {

                phoneData = object.getData().get("phone").toString();
                heightData = object.getData().get("height").toString();
                weightData = object.getData().get("weight").toString();
                emergencyPhoneData = object.getData().get("emergencyPhoneNumber").toString();
                emergencyNameData = object.getData().get("emergencyName").toString();
                emergencyEmailData = object.getData().get("emergencyEmail").toString();
                addressData = object.getData().get("homeAddress").toString();


                String allergies = object.get("allergyData").toString();


                Log.v(TAG, "Allergies: "+ allergies);


                //will retrieve the allergies from firestore
                getFirestoreLists(object, "allergyData", allergiesList);
                //will retrieve the cancer data from firestore
                getFirestoreLists(object, "cancerData", cancerList);
                //will retrieve the surgeries from firestore
                getFirestoreLists(object, "surgeryData", surgeryList);
                //will retrieve the other conditions from firestore
                getFirestoreLists(object, "otherConditionsData", otherConditionsList);
                //will retrieve the diabetes data from firestore
                getFirestoreLists(object, "diabetesData", diabetesList);

                //to check the length of the lists
                Log.v(TAG, "AllergyList Length = " + allergiesList.size());
                Log.v(TAG, "CancerList Length = " + cancerList.size());
                Log.v(TAG, "SurgeryList Length = " + surgeryList.size());
                Log.v(TAG, "DiabetesList Length = " + diabetesList.size());
                Log.v(TAG, "OtherConditionsList Length = " + otherConditionsList.size());
            }

            @Override
            public void getError(Exception e, String msg) {

            }
        });
    }


    private void getFirestoreLists(DocumentSnapshot object, String firestoreName, ArrayList objectList) {

        objectList.clear();

        List<Map<String, Object>> data = (List<Map<String, Object>>) object.get(firestoreName);
        for (Map<String, Object> dataItem : data) {
            String name = dataItem.get("name").toString();
            String doctor = dataItem.get("doctor").toString();
            String date = dataItem.get("date").toString();

            if(firestoreName.equals("allergyData")){
                AllergyInfo allergyItem = new AllergyInfo(name, date, doctor);
                objectList.add(allergyItem);
                Log.v(TAG, "Allergies item: name = " + allergyItem.getName() + " date = " + allergyItem.getDate() + " doctor = " + allergyItem.getDoctor());
            }
            else if(firestoreName.equals("cancerData")){
                CancerInfo cancerItem = new CancerInfo(name, date, doctor);
                objectList.add(cancerItem);
                Log.v(TAG, "Cancer item: name = " + cancerItem.getName() + " date = " + cancerItem.getDate() + " doctor = " + cancerItem.getDoctor());
            }
            else if(firestoreName.equals("surgeryData")){
                SurgeryInfo surgeryItem = new SurgeryInfo(name, date, doctor);
                objectList.add(surgeryItem);
                Log.v(TAG, "Surgery item: name = " + surgeryItem.getName() + " date = " + surgeryItem.getDate() + " doctor = " + surgeryItem.getDoctor());
            }
            else if(firestoreName.equals("diabetesData")){
                DiabetesInfo diabetesItem = new DiabetesInfo(name, date, doctor);
                objectList.add(diabetesItem);
                Log.v(TAG, "Diabetes item: name = " + diabetesItem.getName() + " date = " + diabetesItem.getDate() + " doctor = " + diabetesItem.getDoctor());
            }

            else if(firestoreName.equals("otherConditionsData")){
                OtherConditionsInfo otherConditionItem = new OtherConditionsInfo(name, date, doctor);
                objectList.add(otherConditionItem);
                Log.v(TAG, "Cancer item: name = " + otherConditionItem.getName() + " date = " + otherConditionItem.getDate() + " doctor = " + otherConditionItem.getDoctor());
            }

        }
    }

}
