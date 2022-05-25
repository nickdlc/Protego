package com.example.protego.dashboard.doctor;

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

import com.example.protego.onboarding.PatientAllergiesRecyclerViewAdapter;
import com.example.protego.onboarding.PatientCancerRecyclerViewAdapter;
import com.example.protego.onboarding.PatientDiabetesRecyclerViewAdapter;
import com.example.protego.onboarding.PatientOtherConditionsRecyclerViewAdapter;
import com.example.protego.onboarding.PatientSurgeryRecyclerViewAdapter;
import com.example.protego.R;
import com.example.protego.web.firestore.FirestoreAPI;
import com.example.protego.web.firestore.FirestoreListener;
import com.example.protego.web.schemas.onboarding.AllergyInfo;
import com.example.protego.web.schemas.onboarding.CancerInfo;
import com.example.protego.web.schemas.onboarding.DiabetesInfo;
import com.example.protego.web.schemas.onboarding.OtherConditionsInfo;
import com.example.protego.web.schemas.onboarding.SurgeryInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoctorViewPatientGeneralInfo extends AppCompatActivity {

    public static final String TAG = "PatientGeneralInfoActivity";

    private Button button;
    private FirebaseAuth mAuth;
    public static PatientAllergiesRecyclerViewAdapter allergy_adapter;
    public static PatientSurgeryRecyclerViewAdapter surgery_adapter;
    public static PatientCancerRecyclerViewAdapter cancer_adapter;
    public static PatientDiabetesRecyclerViewAdapter diabetes_adapter;
    public static PatientOtherConditionsRecyclerViewAdapter other_conditions_adapter;
    public String phoneData, heightData, emergencyPhoneData, emergencyNameData, emergencyEmailData, addressData;
    public ArrayList<AllergyInfo> allergiesList = new ArrayList<>();
    public ArrayList<CancerInfo> cancerList = new ArrayList<>();
    public ArrayList<DiabetesInfo> diabetesList = new ArrayList<>();
    public ArrayList<SurgeryInfo> surgeryList = new ArrayList<>();
    public ArrayList<OtherConditionsInfo> otherConditionsList = new ArrayList<>();


    private TextView phoneNumber;
    private TextView address;
    private TextView height;
    private TextView weight;
    private TextView EmergencyName;
    private TextView EmergencyEmail;
    private TextView EmergencyPhoneNumber;

    private String pid;
    private String patientFirst;
    private String patientLast;
    private String onboardingFlag;
    private String name;
    private TextView tvFullName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_general_info);
        mAuth = FirebaseAuth.getInstance();

        connectButtonToActivity(R.id.doctorViewReturnfromGeneralButton, DoctorViewPatientSelections.class);

        Bundle extras = getIntent().getExtras();
        patientFirst = extras.getString("patientFirst");
        patientLast = extras.getString("patientLast");
        name = patientFirst + " " + patientLast;
        pid = extras.getString("patientId");
        onboardingFlag = extras.getString("onboardingFlag");


        tvFullName = findViewById(R.id.doctorViewPatientNameTextLabel);
        tvFullName.setText("Patient Name:" + name);


        phoneNumber = findViewById(R.id.doctorViewPhoneNumberEnter);
        height = findViewById(R.id.doctorViewheightEnter);
        weight = findViewById(R.id.doctorViewWeightEnter);
        EmergencyName = findViewById(R.id.doctorViewEmergencyNameEnter);
        EmergencyEmail = findViewById(R.id.doctorViewEmergencyEmailEnter);
        EmergencyPhoneNumber = findViewById(R.id.doctorViewEmergencyPhoneNumberEnter);
        address = findViewById(R.id.doctorViewAddressEnter);

        LinearLayout allergyLayout = findViewById(R.id.doctorViewAllergyHeading);
        LinearLayout surgeryLayout = findViewById(R.id.doctorViewSurgeryHeading);
        LinearLayout cancerLayout = findViewById(R.id.doctorViewCancerHeading);
        LinearLayout diabetesLayout = findViewById(R.id.doctorViewDiabetesHeading);
        LinearLayout otherConditionLayout = findViewById(R.id.doctorViewOtherConditionHeading);
        LinearLayout informationLayout = findViewById(R.id.doctorViewnformationLayout);

        TextView allergyTitle = findViewById(R.id.doctorViewAllergiesTitle);
        TextView surgeryTitle = findViewById(R.id.doctorViewSurgeriesTitle);
        TextView cancerTitle = findViewById(R.id.doctorViewCancerTitle);
        TextView diabetesTitle = findViewById(R.id.doctorViewDiabetesTitle);
        TextView otherConditionsTitle = findViewById(R.id.doctorViewOtherConditionsTitle);
        TextView noInformationText = findViewById(R.id.doctorViewNoInformationLabel);

        TextView noAllergiesText = findViewById(R.id.doctorViewNoAllergiesText);
        TextView noSurgeriesText = findViewById(R.id.doctorViewNoSurgeriesText);
        TextView noCancerText = findViewById(R.id.doctorViewNoCancerText);
        TextView noDiabetesText = findViewById(R.id.doctorViewNoDiabetesText);
        TextView noOtherConditionText = findViewById(R.id.doctorViewNoOtherConditionText);
        TextView noMedicalInformation = findViewById(R.id.doctorViewNoMedicalInformation);

        RecyclerView allergyRecyclerView = findViewById(R.id.doctorViewGeneralInfoAllergiesRecyclerView);
        RecyclerView surgeryRecyclerView = findViewById(R.id.doctorViewGeneralInfosurgeriesRecyclerView);
        RecyclerView cancerRecyclerView = findViewById(R.id.doctorViewGeneralInfoCancerRecyclerView);
        RecyclerView diabetesRecyclerView = findViewById(R.id.doctorViewGeneralInfoDiabetesRecyclerView);
        RecyclerView otherConditionsRecyclerView = findViewById(R.id.doctorViewGeneralInfoOtherConditionsRecyclerView);



        //display information when the user has completed onboarding
        if (onboardingFlag.equals("true")) {

            EmergencyEmail.setText(DoctorViewPatientSelections.emergencyEmailData);
            EmergencyName.setText(DoctorViewPatientSelections.emergencyNameData);
            EmergencyPhoneNumber.setText(DoctorViewPatientSelections.emergencyPhoneData);
            phoneNumber.setText(DoctorViewPatientSelections.phoneData);
            address.setText(DoctorViewPatientSelections.addressData);
            height.setText(DoctorViewPatientSelections.heightData);
            weight.setText(DoctorViewPatientSelections.weightData);


            //if all medical fields are empty only show required fields and titles
            if(DoctorViewPatientSelections.allergiesList.isEmpty() &&
                    DoctorViewPatientSelections.surgeryList.isEmpty() &&
                    DoctorViewPatientSelections.cancerList.isEmpty() &&
                    DoctorViewPatientSelections.diabetesList.isEmpty() &&
                    DoctorViewPatientSelections.otherConditionsList.isEmpty()
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

            if(!DoctorViewPatientSelections.allergiesList.isEmpty()) { //at least one allergy data

                allergyLayout.setVisibility(View.VISIBLE);
                allergyTitle.setVisibility(View.VISIBLE);
                allergyRecyclerView.setVisibility(View.VISIBLE);
                //setting the recycler views with corresponding data arrays
                allergy_adapter = new PatientAllergiesRecyclerViewAdapter(this, DoctorViewPatientSelections.allergiesList);
                allergyRecyclerView.setAdapter(allergy_adapter);
                allergyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                allergyRecyclerView.setVisibility(View.GONE);
                noAllergiesText.setVisibility(View.VISIBLE);

            }

            if(!DoctorViewPatientSelections.surgeryList.isEmpty()) { //at least one surgery data
                surgeryTitle.setVisibility(View.VISIBLE);
                surgeryLayout.setVisibility(View.VISIBLE);
                surgeryRecyclerView.setVisibility(View.VISIBLE);

                surgery_adapter = new PatientSurgeryRecyclerViewAdapter(this, DoctorViewPatientSelections.surgeryList);
                surgeryRecyclerView.setAdapter(surgery_adapter);
                surgeryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else{
                surgeryRecyclerView.setVisibility(View.GONE);
                noSurgeriesText.setVisibility(View.VISIBLE);
            }


            if(!DoctorViewPatientSelections.cancerList.isEmpty()) { //at least one cancer data
                cancerTitle.setVisibility(View.VISIBLE);
                cancerLayout.setVisibility(View.VISIBLE);
                cancerRecyclerView.setVisibility(View.VISIBLE);

                cancer_adapter = new PatientCancerRecyclerViewAdapter(this, DoctorViewPatientSelections.cancerList);
                cancerRecyclerView.setAdapter(cancer_adapter);
                cancerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }else{
                cancerRecyclerView.setVisibility(View.GONE);
                noCancerText.setVisibility(View.VISIBLE);
            }

            if(!DoctorViewPatientSelections.diabetesList.isEmpty()) { //at least one diabetes data
                diabetesTitle.setVisibility(View.VISIBLE);
                diabetesLayout.setVisibility(View.VISIBLE);
                diabetesRecyclerView.setVisibility(View.VISIBLE);

                diabetes_adapter = new PatientDiabetesRecyclerViewAdapter(this, DoctorViewPatientSelections.diabetesList);
                diabetesRecyclerView.setAdapter(diabetes_adapter);
                diabetesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            } else {
                diabetesRecyclerView.setVisibility(View.GONE);
                noDiabetesText.setVisibility(View.VISIBLE);
            }

            if(!DoctorViewPatientSelections.otherConditionsList.isEmpty()) { //at least one other condition data
                otherConditionsTitle.setVisibility(View.VISIBLE);
                otherConditionLayout.setVisibility(View.VISIBLE);
                otherConditionsRecyclerView.setVisibility(View.VISIBLE);

                other_conditions_adapter = new PatientOtherConditionsRecyclerViewAdapter(this, DoctorViewPatientSelections.otherConditionsList);
                otherConditionsRecyclerView.setAdapter(other_conditions_adapter);
                otherConditionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else{
                otherConditionsRecyclerView.setVisibility(View.GONE);
                noOtherConditionText.setVisibility(View.VISIBLE);
            }

        }

        else{ //when the user skips the onboarding form show
            noInformationText.setText(patientFirst + " has not provided their information. " + patientFirst + "'s information will be updated once they complete onboarding");
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


}