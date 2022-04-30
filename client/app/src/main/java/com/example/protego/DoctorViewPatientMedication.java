package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Doctor;
import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.Patient;
import com.example.protego.web.schemas.PatientDetails;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewPatientMedication
        extends FragmentActivity
        implements PrescribeMedicationFragment.NoticeDialogListener {

    private Button button;
    private Button btnPrescribe;
    public static List<PatientMedicationActivity.MedicationInfo> medicationData;
    public static final String TAG = "DoctorViewPatientMedication";
    private FirebaseAuth mAuth;
    private TextView tvFullName;
    private String pid;
    private String patientFirst;
    private String patientLast;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_medication);
        connectButtonToActivity(R.id.returnFromMedications, DoctorViewPatientSelections.class);
        mAuth = FirebaseAuth.getInstance();
        DoctorViewPatientMedication thisObj = this;
        Bundle extras = getIntent().getExtras();
        patientFirst = extras.getString("patientFirst");
        patientLast = extras.getString("patientLast");
        name = patientFirst + " " + patientLast;
        pid = extras.getString("patientId");

        //System.out.println("Passing through " + pid);

        medicationData = new ArrayList<>();

        tvFullName = findViewById(R.id.medicationPatientFullNameInput);
        tvFullName.setText(name);
        //setUpMedicationInfo();

        btnPrescribe = findViewById(R.id.prescribe_medication);
        btnPrescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prescribeMedication();
            }
        });

        FirestoreAPI.getInstance().getMedications(pid, new FirestoreListener<List<Medication>>() {
            @Override
            public void getResult(List<Medication> mList) {
                String med_id;
                String name;
                String datePrescribed;
                String dosage;
                String prescriber;
                String frequency;

                System.out.println("Medication List : " + mList);

                RecyclerView rvMedicationsForDoctors = findViewById(R.id.doctorViewPatientMedicationRecyclerView);


                for(Medication med : mList){
                    med_id = med.getMedID();
                    name = med.getName();
                    datePrescribed = med.getDatePrescribed().toString();
                    dosage = med.getDosage();
                    prescriber = med.getPrescriber();
                    frequency = med.getFrequency();

                    medicationData.add(new PatientMedicationActivity.MedicationInfo(med_id, name, datePrescribed, dosage, prescriber, frequency));
                    Log.d(TAG, "info : " + med.getName());
                    Log.d(TAG, "info : " + med.getDatePrescribed().toString());
                    Log.d(TAG, "info : " + med.getDosage());
                    Log.d(TAG, "info : " + med.getPrescriber());
                    Log.d(TAG, "info : " + med.getFrequency());
                }
                //medicationData.addAll(Medication.constructMedications(mList));

                // create adapter
                final PatientMedicationRecyclerViewAdapter adapter = new PatientMedicationRecyclerViewAdapter(thisObj,medicationData);
                // Set the adapter on recyclerview
                rvMedicationsForDoctors.setAdapter(adapter);
                // set a layout manager on RV
                rvMedicationsForDoctors.setLayoutManager(new LinearLayoutManager(thisObj));
                Log.d(TAG, "Received request for patients' medication data");
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get medications:\n\t" + msg, e);
                Toast.makeText(DoctorViewPatientMedication.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

    private void prescribeMedication() {
        DialogFragment fragment = new PrescribeMedicationFragment();
        fragment.show(getSupportFragmentManager(), "prescribeMedications");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String name = PrescribeMedicationFragment.med_name;
        String dosage = PrescribeMedicationFragment.med_dosage;
        String frequency = PrescribeMedicationFragment.med_frequency;
        String duid = mAuth.getCurrentUser().getUid();

        if (name != null && dosage != null && frequency != null) {
            FirestoreAPI.getInstance().getDoctor(duid, new FirestoreListener<Doctor>() {
                @Override
                public void getResult(Doctor object) {
                    Log.d(TAG, "Attempting to create medication");
                    String drName = "Dr. " + object.getLastName();
                    createMedication(pid, name, dosage, frequency, drName);
                    dialog.dismiss();
                }

                @Override
                public void getError(Exception e, String msg) {
                    Log.e(TAG, msg, e);
                    Toast.makeText(
                            getApplicationContext(),
                            "Something went wrong when trying to prescribe " + name + ". Please try again!",
                            Toast.LENGTH_LONG
                    );
                }
            });
        } else {
            prescribeMedication();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Use the default behavior set in PrescribeMedicationFragment
    }

    private void createMedication(String puid, String name, String dosage, String frequency, String prescriber) {
        List<String> approvedDoctors = new ArrayList<>();
        approvedDoctors.add(prescriber);
        FirestoreAPI.getInstance().createMedication(puid, approvedDoctors, name, dosage, prescriber, frequency, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                DocumentReference result = (DocumentReference) object.getResult();
                String ID = result.getId();
                result.update("medID", ID); //will update the document's ID field
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
            }
        });
    }

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                i.putExtra("patientFirst", patientFirst);
                i.putExtra("patientLast", patientLast);
                i.putExtra("patientId", pid);
                startActivity(i);
                finish();
            }
        });
    }

}