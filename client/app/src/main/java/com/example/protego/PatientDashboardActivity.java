package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
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

import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.PatientDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientDashboardActivity extends AppCompatActivity{
    public static final String TAG = "PatientDashboardActivity";
    //input fields
    private Button button;
    private LinearLayout layout;
    private ImageButton imageButton;
    private Button btnNotifications;
    private TextView tvNotifications;
    private FirebaseAuth mAuth;
    private PatientDetails patientDetails;
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
        this.patientDetails = new PatientDetails();
        PatientDashboardActivity thisObj = this;

        FirebaseUser currentUser = mAuth.getCurrentUser();

        //updates the navbar to show the patient's first name
        getPatientFirstName(currentUser.getUid());

        PatientVitals.patientData.clear();
        //to get and set the user's vitals for their vitals page
        getPatientVitals(mAuth.getCurrentUser().getUid());


        ServerAPI.getPatient(currentUser.getUid(), new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                if (req != null && !req.getResultString().equals("")) {
                    Log.d(TAG, "req recieved for patient : " + req.getResult().toString());

                    try {
                        JSONObject pateintJSON = req.getResultJSON();

                        patientDetails.firstName = pateintJSON.getString("firstName");
                        Log.d(TAG, "info first name : " + pateintJSON.getString("firstName"));
                    } catch (JSONException e) {
                        Log.e(TAG, "could not recieve doctor info : ", e);
                    }
                } else {
                    Log.d(TAG, "Can't get patient info.");
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

        ServerAPI.getMedication(currentUser.getUid(), new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                try {
                    JSONObject res = req.getResultJSON();
                    patientDetails.heartRate = res.getInt("heartRate");
                    patientDetails.bloodPressure = res.getString("bloodPressure");
                    patientDetails.heightIN = res.getInt("heightIN");
                    patientDetails.weight = res.getInt("weight");

                    RecyclerView recyclerView = findViewById(R.id.patientDataRecyclerView);
                    setUpPatientInfo();

                    PatientDashboardRecyclerViewAdapter adapter = new PatientDashboardRecyclerViewAdapter(thisObj, patientData);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));
                } catch (JSONException e) {
                    Log.e(TAG, "Could not get JSON from request : ", e);
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
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

        //TODO: update this connection to the Medication Activity once it is created
        connectLayoutToActivity(R.id.medicationSelectionLayout, PatientMedicationActivity.class,  bottomSheetDialog);
        //connects the notification notes button to the Notes activity
        connectLayoutToActivity(R.id.notesSelectionLayout, PatientNotesActivity.class,  bottomSheetDialog);
        //TODO: update this connection to the Vitals Activity once it is created
        connectLayoutToActivity(R.id.VitalsSelectionLayout, PatientVitals.class,  bottomSheetDialog);
        //connects the notification View QR Code button to the View QR Code activity
        connectLayoutToActivity(R.id.viewQRCodeSelectionLayout, PatientQRCodeDisplay.class,  bottomSheetDialog);

        bottomSheetDialog.show();
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

        ServerAPI.getPatient(puid, new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest (ServerRequest req){
                if (req != null && !req.getResultString().equals("")) {
                    Log.d(TAG, "req received for patient : " + req.getResult().toString());

                    try {
                        JSONObject patientJSON = req.getResultJSON();

                        patientDetails.firstName = patientJSON.getString("firstName");


                        if (patientDetails.firstName == "" || patientDetails.firstName == null) {
                            Name = "";
                        } else {
                            Name = patientDetails.firstName;
                        }

                        Log.d(TAG, "info first name : " + patientJSON.getString("firstName"));
                    } catch (JSONException e) {
                        Log.e(TAG, "could not receive patient info : ", e);
                    }
                } else {
                    Log.d(TAG, "Can't get patient info.");
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
               Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }
    //function to call from the navbar fragment class to update the navbar according to the patients first name
    public static String getName(){
        return Name;
    }



    private void getPatientVitals(String puid) {
        ServerAPI.getVitals(puid, new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                try {
                    JSONArray res = req.getResultJSONList();

                    String heartRate;
                    String respiratoryRate;
                    String temperature;
                    String puid;
                    String bloodPressure;
                    String source;
                    String date;

                    for(int i = 0; i < res.length(); i++) {

                        JSONObject object = res.getJSONObject(i);

                        heartRate = object.getString("heartRate");
                        respiratoryRate = object.getString("respiratoryRate");
                        temperature = object.getString("temperature");
                        puid = object.getString("patientID");
                        bloodPressure = object.getString("bloodPressure");
                        source = object.getString("source");
                        date = object.getString("date");

                        PatientVitals.patientData.add(new PatientVitals.VitalsInfo(date,source, heartRate, bloodPressure,respiratoryRate,temperature));

                        Log.v(TAG, "object: " + object.toString());

                    }
                    Log.v(TAG, "patient data index 0: " + PatientVitals.patientData.get(0).getSource());
//                    Log.v(TAG, "patient data index 1: " + PatientVitals.patientData.get(1).getSource());

                } catch (JSONException e) {
                    Log.e(TAG, "Could not get JSON from request : ", e);
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(PatientDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }



        });
    }
}
