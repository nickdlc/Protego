package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    public static PatientAllergiesRecyclerViewAdapter allergy_adapter;
    public static PatientSurgeryRecyclerViewAdapter surgery_adapter;
    public static PatientCancerRecyclerViewAdapter cancer_adapter;
    public static PatientDiabetesRecyclerViewAdapter diabetes_adapter;
    public static PatientOtherConditionsRecyclerViewAdapter other_conditions_adapter;
    public String phoneData, heightData, emergencyPhoneData, emergencyNameData, emergencyEmailData, addressData;
    public ArrayList<NewAllergyFragment.AllergyInfo> allergiesList = new ArrayList<>();
    public ArrayList<NewCancerFragment.CancerInfo> cancerList = new ArrayList<>();
    public ArrayList<NewDiabetesFragment.DiabetesInfo> diabetesList = new ArrayList<>();
    public ArrayList<NewSurgeryFragment.SurgeryInfo> surgeryList = new ArrayList<>();
    public ArrayList<NewOtherMedicalConditionsFragment.OtherConditionsInfo> otherConditionsList = new ArrayList<>();


    private TextView phoneNumber;
    private TextView address;
    private TextView height;
    private TextView weight;
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
        weight = findViewById(R.id.weightEnter);
        EmergencyName = findViewById(R.id.EmergencyNameEnter);
        EmergencyEmail = findViewById(R.id.EmergencyEmailEnter);
        EmergencyPhoneNumber = findViewById(R.id.EmergencyPhoneNumberEnter);
        address = findViewById(R.id.addressEnter);

        LinearLayout allergyLayout = findViewById(R.id.allergyHeading);
        LinearLayout surgeryLayout = findViewById(R.id.surgeryHeading);
        LinearLayout cancerLayout = findViewById(R.id.cancerHeading);
        LinearLayout diabetesLayout = findViewById(R.id.diabetesHeading);
        LinearLayout otherConditionLayout = findViewById(R.id.otherConditionHeading);
        LinearLayout informationLayout = findViewById(R.id.informationLayout);

        TextView allergyTitle = findViewById(R.id.allergiesTitle);
        TextView surgeryTitle = findViewById(R.id.surgeriesTitle);
        TextView cancerTitle = findViewById(R.id.CancerTitle);
        TextView diabetesTitle = findViewById(R.id.diabetesTitle);
        TextView otherConditionsTitle = findViewById(R.id.otherConditionsTitle);
        TextView noInformationText = findViewById(R.id.noInformationLabel);

        TextView noAllergiesText = findViewById(R.id.noAllergiesText);
        TextView noSurgeriesText = findViewById(R.id.noSurgeriesText);
        TextView noCancerText = findViewById(R.id.noCancerText);
        TextView noDiabetesText = findViewById(R.id.noDiabetesText);
        TextView noOtherConditionText = findViewById(R.id.noOtherConditionText);
        TextView noMedicalInformation = findViewById(R.id.noMedicalInformation);

        RecyclerView allergyRecyclerView = findViewById(R.id.generalInfoAllergiesRecyclerView);
        RecyclerView surgeryRecyclerView = findViewById(R.id.generalInfosurgeriesRecyclerView);
        RecyclerView cancerRecyclerView = findViewById(R.id.generalInfoCancerRecyclerView);
        RecyclerView diabetesRecyclerView = findViewById(R.id.generalInfoDiabetesRecyclerView);
        RecyclerView otherConditionsRecyclerView = findViewById(R.id.generalInfoOtherConditionsRecyclerView);



        //display information when the user has completed onboarding
        if (PatientOnboardingActivity.flag.equals("true")) {

            EmergencyEmail.setText(PatientDashboardActivity.emergencyEmailData);
            EmergencyName.setText(PatientDashboardActivity.emergencyNameData);
            EmergencyPhoneNumber.setText(PatientDashboardActivity.emergencyPhoneData);
            phoneNumber.setText(PatientDashboardActivity.phoneData);
            address.setText(PatientDashboardActivity.addressData);
            height.setText(PatientDashboardActivity.heightData);
            weight.setText(PatientDashboardActivity.weightData);


            //if all medical fields are empty only show required fields and titles
            if(PatientDashboardActivity.allergiesList.isEmpty() &&
                    PatientDashboardActivity.surgeryList.isEmpty() &&
                    PatientDashboardActivity.cancerList.isEmpty() &&
                    PatientDashboardActivity.diabetesList.isEmpty() &&
                    PatientDashboardActivity.otherConditionsList.isEmpty()
            ){

//                noMedicalInformation.setVisibility(View.VISIBLE);

                allergyLayout.setVisibility(View.GONE);
                surgeryLayout.setVisibility(View.GONE);
                cancerLayout.setVisibility(View.GONE);
                diabetesLayout.setVisibility(View.GONE);
                otherConditionLayout.setVisibility(View.GONE);

//                allergyTitle.setVisibility(View.GONE);
//                surgeryTitle.setVisibility(View.GONE);
//                cancerTitle.setVisibility(View.GONE);
//                diabetesTitle.setVisibility(View.GONE);
//                otherConditionsTitle.setVisibility(View.GONE);


                allergyRecyclerView.setVisibility(View.GONE);
                surgeryRecyclerView.setVisibility(View.GONE);
                cancerRecyclerView.setVisibility(View.GONE);
                diabetesRecyclerView.setVisibility(View.GONE);
                otherConditionsRecyclerView.setVisibility(View.GONE);

            }

            if(!PatientDashboardActivity.allergiesList.isEmpty()) { //at least one allergy data

                allergyLayout.setVisibility(View.VISIBLE);
                allergyTitle.setVisibility(View.VISIBLE);
                allergyRecyclerView.setVisibility(View.VISIBLE);
                //setting the recycler views with corresponding data arrays
                allergy_adapter = new PatientAllergiesRecyclerViewAdapter(this, PatientDashboardActivity.allergiesList);
                allergyRecyclerView.setAdapter(allergy_adapter);
                allergyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                allergyRecyclerView.setVisibility(View.GONE);
                noAllergiesText.setVisibility(View.VISIBLE);

            }

            if(!PatientDashboardActivity.surgeryList.isEmpty()) { //at least one surgery data
                surgeryTitle.setVisibility(View.VISIBLE);
                surgeryLayout.setVisibility(View.VISIBLE);
                surgeryRecyclerView.setVisibility(View.VISIBLE);

                surgery_adapter = new PatientSurgeryRecyclerViewAdapter(this, PatientDashboardActivity.surgeryList);
                surgeryRecyclerView.setAdapter(surgery_adapter);
                surgeryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else{
                surgeryRecyclerView.setVisibility(View.GONE);
                noSurgeriesText.setVisibility(View.VISIBLE);
            }


            if(!PatientDashboardActivity.cancerList.isEmpty()) { //at least one cancer data
                cancerTitle.setVisibility(View.VISIBLE);
                cancerLayout.setVisibility(View.VISIBLE);
                cancerRecyclerView.setVisibility(View.VISIBLE);

                cancer_adapter = new PatientCancerRecyclerViewAdapter(this, PatientDashboardActivity.cancerList);
                cancerRecyclerView.setAdapter(cancer_adapter);
                cancerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }else{
                cancerRecyclerView.setVisibility(View.GONE);
                noCancerText.setVisibility(View.VISIBLE);
            }

            if(!PatientDashboardActivity.diabetesList.isEmpty()) { //at least one diabetes data
                diabetesTitle.setVisibility(View.VISIBLE);
                diabetesLayout.setVisibility(View.VISIBLE);
                diabetesRecyclerView.setVisibility(View.VISIBLE);

                diabetes_adapter = new PatientDiabetesRecyclerViewAdapter(this, PatientDashboardActivity.diabetesList);
                diabetesRecyclerView.setAdapter(diabetes_adapter);
                diabetesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            } else {
                diabetesRecyclerView.setVisibility(View.GONE);
                noDiabetesText.setVisibility(View.VISIBLE);
            }

            if(!PatientDashboardActivity.otherConditionsList.isEmpty()) { //at least one other condition data
                otherConditionsTitle.setVisibility(View.VISIBLE);
                otherConditionLayout.setVisibility(View.VISIBLE);
                otherConditionsRecyclerView.setVisibility(View.VISIBLE);

                other_conditions_adapter = new PatientOtherConditionsRecyclerViewAdapter(this, PatientDashboardActivity.otherConditionsList);
                otherConditionsRecyclerView.setAdapter(other_conditions_adapter);
                otherConditionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else{
                otherConditionsRecyclerView.setVisibility(View.GONE);
                noOtherConditionText.setVisibility(View.VISIBLE);
            }

        }

        else{ //when the user skips the onboarding form show
            noInformationText.setVisibility(View.VISIBLE); //show message instead of recycler views and titles to organize onboarding components
            informationLayout.setVisibility(View.GONE);
        }

    }


    private void getOnboardingDetails(String id){

        FirestoreAPI.getInstance().getOnboardingDetails(id, new FirestoreListener<DocumentSnapshot>() {
            @Override
            public void getResult(DocumentSnapshot object) {

                phoneData = object.getData().get("phone").toString();
                heightData =object.getData().get("height").toString();
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

        List<Map<String, Object>> data = (List<Map<String, Object>>) object.get(firestoreName);
        for (Map<String, Object> dataItem : data) {
            String name = dataItem.get("name").toString();
            String doctor = dataItem.get("doctor").toString();
            String date = dataItem.get("date").toString();

            if(firestoreName.equals("allergyData")){
                NewAllergyFragment.AllergyInfo allergyItem = new NewAllergyFragment.AllergyInfo(name, date, doctor);
                objectList.add(allergyItem);
                Log.v(TAG, "Allergies item: name = " + allergyItem.getName() + " date = " + allergyItem.getDate() + " doctor = " + allergyItem.getDoctor());
            }
            else if(firestoreName.equals("cancerData")){
                NewCancerFragment.CancerInfo cancerItem = new NewCancerFragment.CancerInfo(name, date, doctor);
                objectList.add(cancerItem);
                Log.v(TAG, "Cancer item: name = " + cancerItem.getName() + " date = " + cancerItem.getDate() + " doctor = " + cancerItem.getDoctor());
            }
            else if(firestoreName.equals("surgeryData")){
                NewSurgeryFragment.SurgeryInfo surgeryItem = new NewSurgeryFragment.SurgeryInfo(name, date, doctor);
                objectList.add(surgeryItem);
                Log.v(TAG, "Surgery item: name = " + surgeryItem.getName() + " date = " + surgeryItem.getDate() + " doctor = " + surgeryItem.getDoctor());
            }
            else if(firestoreName.equals("diabetesData")){
                NewDiabetesFragment.DiabetesInfo diabetesItem = new NewDiabetesFragment.DiabetesInfo(name, date, doctor);
                objectList.add(diabetesItem);
                Log.v(TAG, "Diabetes item: name = " + diabetesItem.getName() + " date = " + diabetesItem.getDate() + " doctor = " + diabetesItem.getDoctor());
            }

            else if(firestoreName.equals("otherConditionsData")){
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