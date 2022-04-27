package com.example.protego;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.web.schemas.Doctor;

public class PatientOnboardingActivity extends AppCompatActivity implements NoticeDialogListener{

    DialogFragment fragment;
    public static PatientAllergiesRecyclerViewAdapter allergy_adapter;
    public static View allergyView;
    public static View surgeryView;
    public static View cancerView;
    public static View diabetesView;
    public static View otherConditionView;
    private static String[] feetArray = {"Select Feet", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets first time in dashboard to false so
        // user doesn't get taken back to onboarding when going to dashboard
        PatientDashboardActivity.firstTime = false;

        setContentView(R.layout.patient_onboarding);
        connectButtonToActivity(R.id.skipForNowBtn,PatientDashboardActivity.class);

        //the spinner component
        Spinner feetSpinner = (Spinner) findViewById(R.id.height_feet_spinner);



        //ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, feetArray);
        //specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        feetSpinner.setAdapter(adapter);
        feetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

           //String userType = (String) parent.getItemAtPosition(pos);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }


        });


        findViewById(R.id.addAllergyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAllergy();
            }
        });

        findViewById(R.id.addSurgeryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSurgery();
            }
        });

        findViewById(R.id.addCancerInformationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCancer();
            }
        });

        findViewById(R.id.addDiabetesInformationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDiabetes();
            }
        });


        findViewById(R.id.addMedicalConditionsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOtherMedicalInformation();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.allergiesRecyclerView);
        allergy_adapter = new PatientAllergiesRecyclerViewAdapter(this,NewAllergyFragment.allergyData);
        recyclerView.setAdapter(allergy_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allergyView = (LinearLayout) findViewById(R.id.allergyHeading);
        surgeryView = (LinearLayout) findViewById(R.id.surgeryHeading);
        diabetesView = (LinearLayout) findViewById(R.id.diabetesHeading);
        cancerView = (LinearLayout) findViewById(R.id.cancerHeading);
        otherConditionView = (LinearLayout) findViewById(R.id.otherConditionHeading);

    }

    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {
        Button button;
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


    private void addAllergy(){

        DialogFragment fragment = new NewAllergyFragment();
        fragment.show(getSupportFragmentManager(), "allergy");

    }
    private void addCancer(){
        DialogFragment fragment = new NewCancerFragment();
        fragment.show(getSupportFragmentManager(), "cancer");
    }
    private void addDiabetes(){
        DialogFragment fragment = new NewDiabetesFragment();
        fragment.show(getSupportFragmentManager(), "diabetes");
    }
    private void addSurgery(){
        fragment = new NewSurgeryFragment();
        fragment.show(getSupportFragmentManager(), "surgery");
    }
    private void addOtherMedicalInformation(){
        DialogFragment fragment = new NewOtherMedicalConditionsFragment();
        fragment.show(getSupportFragmentManager(), "otherMedicalInformation");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        if(dialog.getTag() == "surgery"){
            addSurgery();

        }
        else if (dialog.getTag() == "allergy"){
            addAllergy();

        }
        else if (dialog.getTag() == "cancer") {
            addCancer();
        }
        else if (dialog.getTag() == "diabetes"){
            addDiabetes();
        }
        else if (dialog.getTag() == "otherMedicalInformation") {
            addOtherMedicalInformation();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }
}

