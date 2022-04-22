package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Date;
import java.util.List;

public class PatientMedicationActivity extends FragmentActivity
        implements NewMedicationFragment.NoticeDialogListener {
    public static ArrayList<MedicationInfo> medicationData = new ArrayList<>();
//    public static ArrayList<MedicationInfo> medicationFormatted = new ArrayList<>();
    public static final String TAG = "PatientMedicationActivity";
    private FirebaseAuth mAuth;
    public static String userID;



    public static class MedicationInfo {
        private final String med_id,name,date,dosage,prescribedBy;

        public MedicationInfo(String med_id, String name, String date, String dosage, String prescribedBy) {
            this.med_id = med_id;
            this.name = name;
            this.date = date;
            this.dosage = dosage;
            this.prescribedBy = prescribedBy;
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

    }


    private void setUpMedicationInfo() {
        medicationData.add(new MedicationInfo("1","Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("2","Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("3","Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("4","Jay","1/01/2022","A lot","Dr. M"));
        medicationData.add(new MedicationInfo("5","Jay","1/01/2022","A lot","Dr. M"));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_medication);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

        //setUpMedicationInfo();

        PatientMedicationRecyclerViewAdapter adapter = new PatientMedicationRecyclerViewAdapter(this,medicationData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        findViewById(R.id.add_medication).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedications();
            }
        });
    }


    private void getPatientMedications(String puid) {
        FirestoreAPI.getInstance().getMedications(puid, new FirestoreListener<List<Medication>>() {
            @Override
            public void getResult(List<Medication> medications) {
                String med_id;
                String name;
                String datePrescribed;
                String dosage;
                String prescriber;


                for(Medication med : medications) {
                    med_id = med.getMedID();
                    name = med.getName();
                    datePrescribed = med.getDatePrescribed().toString();
                    dosage = med.getDosage();
                    prescriber = med.getPrescriber();

                    medicationData
                            .add(new PatientMedicationActivity.MedicationInfo(med_id,name,datePrescribed,dosage,prescriber));
                    Log.v(TAG, "object: " + med.toString());
                }

                Log.v(TAG, "medicationData: " + medicationData.get(0).name);
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get medications for patient:\n\t" + msg, e);
                Toast.makeText(PatientMedicationActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

    private void createMedication(String prescribee, String name, String dosage, String prescriber) {
        FirestoreAPI.getInstance().createMedication(prescribee, RandomGenerator.randomApprovedDoctors(), name, dosage, prescriber, new FirestoreListener<Task>() {
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

        if(name != null && dosage != null && prescriber != null) {
            createMedication(userID, name, dosage, prescriber);
            dialog.dismiss();

            Intent i = new Intent(this, PatientDashboardActivity.class);
            startActivity(i);

        } else {
            addMedications(); //creates a new note dialog when a user does not complete the note title and content fields
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

}
