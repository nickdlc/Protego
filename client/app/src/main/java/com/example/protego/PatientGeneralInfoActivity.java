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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatientGeneralInfoActivity extends AppCompatActivity {

    public static final String TAG = "PatientGeneralInfoActivity";

    private Button button;
    private FirebaseAuth mAuth;
    public final static String[] phoneData = new String[1];
    public final static String[] heightData = new String[1];
    public final static String[] emergencyPhoneData = new String[1];
    public final static String[] emergencyNameData = new String[1];
    public final static String[] emergencyEmailData = new String[1];
    public final static String[] addressData = new String[1];
    public ArrayList<NewAllergyFragment.AllergyInfo> allergiesList = new ArrayList<>();
    public ArrayList<NewCancerFragment.CancerInfo> cancerList = new ArrayList<>();
    public ArrayList<NewDiabetesFragment.DiabetesInfo> diabetesList = new ArrayList<>();
    public ArrayList<NewSurgeryFragment.SurgeryInfo> surgeryList = new ArrayList<>();
    public ArrayList<NewOtherMedicalConditionsFragment.OtherConditionsInfo> otherConditionsList = new ArrayList<>();


    private TextView phoneNumber;
    private TextView address;
    private TextView height;
    private TextView EmergencyName;
    private TextView EmergencyEmail;
    private TextView EmergencyPhoneNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_general_info);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        connectButtonToActivity(R.id.returnfromGeneralButton, PatientDashboardActivity.class);


        phoneNumber = findViewById(R.id.phoneNumberEnter);
        height = findViewById(R.id.heightEnter);
        EmergencyName = findViewById(R.id.EmergencyNameEnter);
        EmergencyEmail = findViewById(R.id.EmergencyEmailEnter);
        EmergencyPhoneNumber = findViewById(R.id.EmergencyPhoneNumberEnter);
        address = findViewById(R.id.addressEnter);

//        getOnboardingDetails(id);

        if (PatientOnboardingActivity.flag == "true") {

            EmergencyEmail.setText(PatientDashboardActivity.emergencyEmailData[0]);
            EmergencyName.setText(PatientDashboardActivity.emergencyNameData[0]);
            EmergencyPhoneNumber.setText(PatientDashboardActivity.emergencyPhoneData[0]);
            phoneNumber.setText(PatientDashboardActivity.phoneData[0]);
            address.setText(PatientDashboardActivity.addressData[0]);
            height.setText(PatientDashboardActivity.heightData[0]);

        }

    }


    private void getOnboardingDetails(String id){

        FirestoreAPI.getInstance().getOnboardingDetails(id, new FirestoreListener<DocumentSnapshot>() {
            @Override
            public void getResult(DocumentSnapshot object) {

                phoneData[0] = object.get("phone").toString();
                heightData[0] = object.get("height").toString();
                emergencyPhoneData[0] = object.get("emergencyPhoneNumber").toString();
                emergencyNameData[0] = object.get("emergencyName").toString();
                emergencyEmailData[0] = object.get("emergencyEmail").toString();
                addressData[0] = object.get("homeAddress").toString();


                //TODO: Display allergies, cancer, surgeries, diabetes and other medical conditions
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

        List<Map<String, Object>> data = (List<Map<String, Object>>) object.get(firestoreName);
        for (Map<String, Object> dataItem : data) {
            String name = dataItem.get("name").toString();
            String doctor = dataItem.get("doctor").toString();
            String date = dataItem.get("date").toString();

            if(firestoreName == "allergyData"){
                NewAllergyFragment.AllergyInfo allergyItem = new NewAllergyFragment.AllergyInfo(name, date, doctor);
                objectList.add(allergyItem);
                Log.v(TAG, "Allergies item: name = " + allergyItem.getName() + " date = " + allergyItem.getDate() + " doctor = " + allergyItem.getDoctor());
            }
            else if(firestoreName == "cancerData"){
                NewCancerFragment.CancerInfo cancerItem = new NewCancerFragment.CancerInfo(name, date, doctor);
                objectList.add(cancerItem);
                Log.v(TAG, "Cancer item: name = " + cancerItem.getName() + " date = " + cancerItem.getDate() + " doctor = " + cancerItem.getDoctor());
            }
            else if(firestoreName == "surgeryData"){
                NewSurgeryFragment.SurgeryInfo surgeryItem = new NewSurgeryFragment.SurgeryInfo(name, date, doctor);
                objectList.add(surgeryItem);
                Log.v(TAG, "Surgery item: name = " + surgeryItem.getName() + " date = " + surgeryItem.getDate() + " doctor = " + surgeryItem.getDoctor());
            }
            else if(firestoreName == "diabetesData"){
                NewDiabetesFragment.DiabetesInfo diabetesItem = new NewDiabetesFragment.DiabetesInfo(name, date, doctor);
                objectList.add(diabetesItem);
                Log.v(TAG, "Diabetes item: name = " + diabetesItem.getName() + " date = " + diabetesItem.getDate() + " doctor = " + diabetesItem.getDoctor());
            }

            else if(firestoreName == "otherConditionsData"){
                NewOtherMedicalConditionsFragment.OtherConditionsInfo otherConditionItem = new NewOtherMedicalConditionsFragment.OtherConditionsInfo(name, date, doctor);
                objectList.add(otherConditionItem);
                Log.v(TAG, "Cancer item: name = " + otherConditionItem.getName() + " date = " + otherConditionItem.getDate() + " doctor = " + otherConditionItem.getDoctor());
            }

        }
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

}