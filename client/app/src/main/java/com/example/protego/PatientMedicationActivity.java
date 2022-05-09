package com.example.protego;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.protego.util.RandomGenerator;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.Medication;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PatientMedicationActivity extends FragmentActivity
        implements NewMedicationFragment.NoticeDialogListener {
    public static List<MedicationInfo> medicationData;
//    public static ArrayList<MedicationInfo> medicationFormatted = new ArrayList<>();
    public static final String TAG = "PatientMedicationActivity";
    private FirebaseAuth mAuth;
    public static String userID;
    private SwipeRefreshLayout swipeContainer;
    public static AlarmManager am;

    /*
    //used for register alarm manager
    PendingIntent pendingIntent;
    //used to store running alarmmanager instance
    AlarmManager alarmManager;
    //Callback function for Alarmmanager event
    BroadcastReceiver mReceiver;

    public void onClickSetAlarm(View view) {
        //Get the current time and set alarm after 10 seconds from current time
        // so here we get
        alarmManager.set( AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 , pendingIntent );
    }

    private void RegisterAlarmBroadcast() {
        Log.i("Alarm Example:RegisterAlarmBroadcast()", "Going to register Intent.RegisterAlarmBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        mReceiver = new BroadcastReceiver()
        {
            private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.i(TAG,"BroadcastReceiver::OnReceive() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Toast.makeText(context, "Don't forget to take your medication today!", Toast.LENGTH_LONG).show();
            }
        };

        // register the alarm broadcast here
        registerReceiver(mReceiver, new IntentFilter("com.myalarm.alarmexample") );
        pendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.myalarm.alarmexample"),0 );
        alarmManager = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE ));
    }
/*
    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(pendingIntent);
        //getBaseContext().unregisterReceiver(mReceiver);
    }*/
/*
    @Override
    protected void onDestroy() {
        //unregisterReceiver(mReceiver);
        super.onDestroy();
    }
*/

    public static class MedicationInfo {
        private final String med_id, name, date, dosage, prescribedBy, frequency;

        public MedicationInfo(String med_id, String name, String date, String dosage, String prescribedBy, String frequency) {
            this.med_id = med_id;
            this.name = name;
            this.date = date;
            this.dosage = dosage;
            this.prescribedBy = prescribedBy;
            this.frequency = frequency;
        }

        public String getName() {
            return this.name;
        }

        public String getDate() {
            return this.date;
        }

        public String getDosage() {
            return this.dosage;
        }

        public String getPrescribedBy() {
            return this.prescribedBy;
        }

        public String getId() {
            return this.med_id;
        }

        public String getFrequency() { return this.frequency; }

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_medication);

        medicationData = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        //RegisterAlarmBroadcast();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.HOUR_OF_DAY, 0);

        Intent intent1 = new Intent(PatientMedicationActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PatientMedicationActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        /*findViewById(R.id.add_medication).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         }
        });*/

        PatientMedicationActivity thisObj = this;
        final PatientMedicationRecyclerViewAdapter adapter = new PatientMedicationRecyclerViewAdapter(thisObj,medicationData);

        FirestoreAPI.getInstance().getMedications(userID, new FirestoreListener<List<Medication>>() {
            @Override
            public void getResult(List<Medication> medications) {
                String med_id;
                String name;
                String datePrescribed;
                String dosage;
                String prescriber;
                String frequency;

                RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

                for(Medication med : medications) {
                    med_id = med.getMedID();
                    name = med.getName();
                    datePrescribed = med.getDatePrescribed().toString();
                    dosage = med.getDosage();
                    prescriber = med.getPrescriber();
                    frequency = med.getFrequency();

                    medicationData
                            .add(new MedicationInfo(med_id, name, datePrescribed, dosage, prescriber, frequency));
                }

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get medications for patient:\n\t" + msg, e);
                Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getPatientMedications(userID);
                adapter.clear();
                FirestoreAPI.getInstance().getMedications(userID, new FirestoreListener<List<Medication>>() {
                    @Override
                    public void getResult(List<Medication> medications) {
                        String med_id;
                        String name;
                        String datePrescribed;
                        String dosage;
                        String prescriber;
                        String frequency;

                        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

                        for(Medication med : medications) {
                            med_id = med.getMedID();
                            name = med.getName();
                            datePrescribed = med.getDatePrescribed().toString();
                            dosage = med.getDosage();
                            prescriber = med.getPrescriber();
                            frequency = med.getFrequency();

                            medicationData
                                    .add(new MedicationInfo(med_id, name, datePrescribed, dosage, prescriber, frequency));
                        }

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));

                        //adapter.addAll(medicationData);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void getError(Exception e, String msg) {
                        Log.e(TAG, "Failed to get medications for patient:\n\t" + msg, e);
                        Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
                    }
                });
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        findViewById(R.id.add_medication).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedications();
            }
        });

    }

    /*private void getPatientMedications(String puid) {

    }*/

    private void createMedication(String prescribee, String name, String dosage, String prescriber, String frequency) {
        FirestoreAPI.getInstance().createMedication(prescribee, RandomGenerator.randomApprovedDoctors(), name, dosage, prescriber, frequency, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                // do nothing, just generate data
                DocumentReference result = (DocumentReference) object.getResult();
                String ID = result.getId();
                result.update("medID", ID); //will update the document's ID field
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }


    private void addMedications() {
        DialogFragment fragment = new NewMedicationFragment();
        fragment.show(getSupportFragmentManager(), "addMedications");
    }

    //allows the activity to create a new note when the user selects the "add" button on the dialog
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String name = NewMedicationFragment.med_name;
        String dosage = NewMedicationFragment.med_dosage;
        String prescriber = NewMedicationFragment.med_prescriber;
        String frequency = NewMedicationFragment.med_frequency;

        if(name != null && dosage != null && prescriber != null) {
            createMedication(userID, name, dosage, prescriber, frequency);
            dialog.dismiss();

            //Intent i = new Intent(this, PatientDashboardActivity.class);
            //startActivity(i);

        } else {
            addMedications(); //creates a new medication dialog when a user does not complete all the fields
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

}
