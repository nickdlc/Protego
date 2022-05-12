package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Onboarding.AllergyInfo;
import com.example.protego.web.schemas.Onboarding.CancerInfo;
import com.example.protego.web.schemas.Onboarding.DiabetesInfo;
import com.example.protego.web.schemas.Onboarding.OtherConditionsInfo;
import com.example.protego.web.schemas.Onboarding.SurgeryInfo;
import com.example.protego.web.schemas.PatientDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoctorViewPatientSelections extends AppCompatActivity {
    public static final String TAG = "DoctorViewPatientSelections";

    // input fields here
    private FirebaseAuth mAuth;
    private Button button;
    private TextView tvPatientName;
    private String patientFirst;
    private String patientLast;
    private String onboardingFlag;
    private String name;
    private String pid;

    public static String phoneData, heightData, weightData, emergencyPhoneData, emergencyNameData, emergencyEmailData, addressData;
    public static ArrayList<AllergyInfo> allergiesList = new ArrayList<>();
    public static ArrayList<CancerInfo> cancerList = new ArrayList<>();
    public static ArrayList<DiabetesInfo> diabetesList = new ArrayList<>();
    public static ArrayList<SurgeryInfo> surgeryList = new ArrayList<>();
    public static ArrayList<OtherConditionsInfo> otherConditionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_selections);
        connectButtonToActivity(R.id.returnButton, DoctorViewPatientsActivity.class);
//        connectButtonToActivity(R.id.generalButton, DoctorViewPatientProfile.class);
        connectButtonToActivity(R.id.generalButton, DoctorViewPatientGeneralInfo.class);
        connectButtonToActivity(R.id.medicationButton, DoctorViewPatientMedication.class);
        connectButtonToActivity(R.id.notesButton, DoctorViewPatientNotes.class);
        connectButtonToActivity(R.id.vitalsButton, DoctorViewPatientVitals.class);
        Bundle extras = getIntent().getExtras();
        patientFirst = extras.getString("patientFirst");
        patientLast = extras.getString("patientLast");
        onboardingFlag = extras.getString("onboardingFlag");
        name = patientFirst + " " + patientLast;
        pid = extras.getString("patientId");
        //patientName += extras.get("patientLast");

        tvPatientName = findViewById(R.id.selectionsPatientFullNameInput);
        tvPatientName.setText(name);



        Log.v(TAG, "Onboarding Flag from selections: "+ onboardingFlag);


        //to get the user's onboarding detail if the form has been completed
        if(onboardingFlag.equals("true")){
            getOnboardingDetails(pid);
        }
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
                i.putExtra("onboardingFlag", onboardingFlag);
                startActivity(i);
                finish();
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