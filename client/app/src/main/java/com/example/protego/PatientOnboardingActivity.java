package com.example.protego;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.util.RandomGenerator;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Doctor;
import com.example.protego.web.schemas.Onboarding.OnboardingInfo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Date;

public class PatientOnboardingActivity extends AppCompatActivity implements NoticeDialogListener {

    DialogFragment fragment;
    public static PatientAllergiesRecyclerViewAdapter allergy_adapter;
    public static PatientSurgeryRecyclerViewAdapter surgery_adapter;
    public static PatientCancerRecyclerViewAdapter cancer_adapter;
    public static PatientDiabetesRecyclerViewAdapter diabetes_adapter;
    public static PatientOtherConditionsRecyclerViewAdapter other_conditions_adapter;
    private FirebaseAuth mAuth;
    private String userID;
    public static final String TAG = "PatientOnboardingActivity";
    public static OnboardingInfo onboardingInfo;

    public static View allergyView;
    public static View surgeryView;
    public static View cancerView;
    public static View diabetesView;
    public static View otherConditionView;
    private String Height_Inches, Height_Feet;
    private static String[] feetArray = {"Select Feet", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets first time in dashboard to false so
        // user doesn't get taken back to onboarding when going to dashboard
        PatientDashboardActivity.firstTime = false;

        setContentView(R.layout.patient_onboarding);
        connectButtonToActivity(R.id.skipForNowBtn, PatientDashboardActivity.class);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

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
                Height_Feet = (String) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        //setting the recycler views with corresponding data arrays
        RecyclerView allergyRecyclerView = findViewById(R.id.allergiesRecyclerView);
        allergy_adapter = new PatientAllergiesRecyclerViewAdapter(this, NewAllergyFragment.allergyData);
        allergyRecyclerView.setAdapter(allergy_adapter);
        allergyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView surgeryRecyclerView = findViewById(R.id.surgeriesRecyclerView);
        surgery_adapter = new PatientSurgeryRecyclerViewAdapter(this, NewSurgeryFragment.SurgeryData);
        surgeryRecyclerView.setAdapter(surgery_adapter);
        surgeryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView cancerRecyclerView = findViewById(R.id.cancerRecyclerView);
        cancer_adapter = new PatientCancerRecyclerViewAdapter(this, NewCancerFragment.CancerData);
        cancerRecyclerView.setAdapter(cancer_adapter);
        cancerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView diabetesRecyclerView = findViewById(R.id.diabetesRecyclerView);
        diabetes_adapter = new PatientDiabetesRecyclerViewAdapter(this, NewDiabetesFragment.DiabetesData);
        diabetesRecyclerView.setAdapter(diabetes_adapter);
        diabetesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView otherConditionsRecyclerView = findViewById(R.id.otherConditionsRecyclerView);
        other_conditions_adapter = new PatientOtherConditionsRecyclerViewAdapter(this, NewOtherMedicalConditionsFragment.OtherConditionsData);
        otherConditionsRecyclerView.setAdapter(other_conditions_adapter);
        otherConditionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //headings which will appear above recycler views
        allergyView = (LinearLayout) findViewById(R.id.allergyHeading);
        surgeryView = (LinearLayout) findViewById(R.id.surgeryHeading);
        diabetesView = (LinearLayout) findViewById(R.id.diabetesHeading);
        cancerView = (LinearLayout) findViewById(R.id.cancerHeading);
        otherConditionView = (LinearLayout) findViewById(R.id.otherConditionHeading);


        //Submit button
        Button button = findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //all other onboarding fields

                //fields from the General Section
                EditText DOB_edit = (EditText) findViewById(R.id.DOB_EditText);
                EditText Email_edit = (EditText) findViewById(R.id.Email_EditText);
                EditText Phone_edit = (EditText) findViewById(R.id.phoneNumber_EditText);
                EditText Home_Address_edit = (EditText) findViewById(R.id.home_Address_EditText);

                String DOB = DOB_edit.getText().toString();

                Log.v(TAG, "DOB String" + DOB);

                String Email = Email_edit.getText().toString();
                String Phone = Phone_edit.getText().toString();
                String Home_Address = Home_Address_edit.getText().toString();

                //fields from the Emergency section
                EditText Emergency_Name_edit = (EditText) findViewById(R.id.emergency_contact_fullName_EditText);
                EditText Emergency_Number_edit = (EditText) findViewById(R.id.emergency_contact_number_EditText);
                EditText Emergency_Email_edit = (EditText) findViewById(R.id.emergency_contact_email_EditText);

                String Emergency_Name = Emergency_Name_edit.getText().toString();
                String Emergency_Number = Emergency_Number_edit.getText().toString();
                String Emergency_Email = Emergency_Email_edit.getText().toString();

                //field from medical - the height of a user inches component
                EditText Height_Inches_edit = (EditText) findViewById(R.id.InchesEditText);
                Height_Inches = Height_Inches_edit.getText().toString();
                String Height = Height_Feet + "'" + Height_Inches + "Feet";

                if (emptyFields(DOB, Email, Phone, Home_Address, Emergency_Name,
                        Emergency_Number, Emergency_Email, Height_Feet, Height_Inches)) {
                    Toast.makeText(PatientOnboardingActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                }
                else if(notInchesFormat(Height_Inches)){
                    Toast.makeText(PatientOnboardingActivity.this, "Please complete the Height Inches field correctly", Toast.LENGTH_SHORT).show();
                }
                else if (notDateFormat(DOB)) {
                    Toast.makeText(PatientOnboardingActivity.this, "Please complete the Date of Birth field correctly", Toast.LENGTH_SHORT).show();

                }
                else if (notPhoneNumberFormat(Phone, Emergency_Number)) {
                    Toast.makeText(PatientOnboardingActivity.this, "Please complete the phone number fields correctly", Toast.LENGTH_SHORT).show();
                }
                else if (notEmailFormat(Email, Emergency_Email)) {
                    Toast.makeText(PatientOnboardingActivity.this, "Please complete the email field correctly", Toast.LENGTH_SHORT).show();
                }

                else {

                    onboardingInfo = new OnboardingInfo(userID, DOB, Email, Phone, Home_Address, Emergency_Name,
                            Emergency_Number, Emergency_Email, Height,
                            NewAllergyFragment.allergyData,
                            NewSurgeryFragment.SurgeryData,
                            NewCancerFragment.CancerData,
                            NewDiabetesFragment.DiabetesData,
                            NewOtherMedicalConditionsFragment.OtherConditionsData);

                    //connect to Firestore
                    createOnboarding(onboardingInfo);

                    Intent i = new Intent(PatientOnboardingActivity.this, PatientDashboardActivity.class);
                    startActivity(i);
                }
            }
        });
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


    private void addAllergy() {

        DialogFragment fragment = new NewAllergyFragment();
        fragment.show(getSupportFragmentManager(), "allergy");

    }

    private void addCancer() {
        DialogFragment fragment = new NewCancerFragment();
        fragment.show(getSupportFragmentManager(), "cancer");
    }

    private void addDiabetes() {
        DialogFragment fragment = new NewDiabetesFragment();
        fragment.show(getSupportFragmentManager(), "diabetes");
    }

    private void addSurgery() {
        fragment = new NewSurgeryFragment();
        fragment.show(getSupportFragmentManager(), "surgery");
    }

    private void addOtherMedicalInformation() {
        DialogFragment fragment = new NewOtherMedicalConditionsFragment();
        fragment.show(getSupportFragmentManager(), "otherMedicalInformation");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        //when the dialog is not completed correctly show a new dialog
        if (dialog.getTag() == "surgery") {
            addSurgery();
        } else if (dialog.getTag() == "allergy") {
            addAllergy();
        } else if (dialog.getTag() == "cancer") {
            addCancer();
        } else if (dialog.getTag() == "diabetes") {
            addDiabetes();
        } else if (dialog.getTag() == "otherMedicalInformation") {
            addOtherMedicalInformation();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }


    private void createOnboarding(OnboardingInfo onboardingInfo) {
        FirestoreAPI.getInstance().createOnboarding(onboardingInfo, new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                // do nothing, just generate data
                DocumentReference result = (DocumentReference) object.getResult();
                String ID = result.getId();
                result.update("onboardingID", ID); //will update the document's ID field
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(PatientOnboardingActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

    }

    private boolean emptyFields(String DOB, String Email, String Phone, String Home_Address, String Emergency_Name,
                                String Emergency_Number, String Emergency_Email, String HeightFeet, String HeightInches) {


        boolean fieldsIncorrect = true;


        if (!DOB.isEmpty() && !Email.isEmpty() && !Phone.isEmpty() && !Home_Address.isEmpty()
        && !Emergency_Name.isEmpty() && !Emergency_Number.isEmpty() && !Emergency_Email.isEmpty() && !HeightFeet.equals(feetArray[0])
        ){
            fieldsIncorrect =  false; //the fields are in the correct format
        } else {
            fieldsIncorrect = true; //the fields are not in the correct format
        }

        return fieldsIncorrect;
    }

    private boolean notInchesFormat(String HeightInches){
        boolean inchesFieldsIncorrect = true;
        char[] inchArray = HeightInches.toCharArray();

        Log.v(TAG, "Inches length" + inchArray.length);
        Log.v(TAG, "Inches Array" + inchArray);



        if(inchArray.length == 1 ){
            if(checkDigit(inchArray[0])){
                inchesFieldsIncorrect = false; //correct format
            }
        }
        else if (inchArray.length == 2) {
            Log.v(TAG, "Inches Array [0]" + inchArray[0]);
            Log.v(TAG, "Inches Array [1]" + inchArray[1]);
            if (checkDigit(inchArray[0]) && checkDigit(inchArray[1])) {
                Log.v(TAG, "Inches [0] test" + inchArray[0]);
                Log.v(TAG, "Inches [1] test" + inchArray[1]);
                if (inchArray[0] == '1' && (inchArray[1] == '1' || inchArray[1] == '0' ) ) {
                    inchesFieldsIncorrect = false;
                    Log.v(TAG, "Inches [0]" + inchArray[0]);
                    Log.v(TAG, "Inches [1]" + inchArray[1]);
                }
            }
        }
        else{
            inchesFieldsIncorrect = true;
        }

        return inchesFieldsIncorrect;
    }

    private boolean notDateFormat(String DOB) {

        char[] DOBArray =  DOB.toCharArray();
        if( DOBArray.length == 10 && DOBArray[2] == '/' && DOBArray[5] == '/'
                && checkDigit(DOBArray[0]) && checkDigit(DOBArray[1])
                && checkDigit(DOBArray[3]) && checkDigit(DOBArray[4])
                && checkDigit(DOBArray[6]) && checkDigit(DOBArray[7])
                && checkDigit(DOBArray[8]) && checkDigit(DOBArray[9])
        ){
            return false; //the fields are in the correct format
        }
        return true; //the fields are not in the correct format

    }

    private boolean notPhoneNumberFormat(String Phone, String Emergency_Number) {

        char[] phoneArray = Phone.toCharArray();
        char[] emergencyPhoneArray = Emergency_Number.toCharArray();

        if( //check the user's phone number format
            phoneArray.length == 12 && phoneArray[3] == '-' && phoneArray[7] == '-'
                    && checkDigit(phoneArray[0]) && checkDigit(phoneArray[1])
                    && checkDigit(phoneArray[2]) && checkDigit(phoneArray[4])
                    && checkDigit(phoneArray[5]) && checkDigit(phoneArray[6])
                    && checkDigit(phoneArray[8]) && checkDigit(phoneArray[9])
                    && checkDigit(phoneArray[10]) && checkDigit(phoneArray[11])
            //check the user's emergency contact phone number format
                    && emergencyPhoneArray.length == 12 && emergencyPhoneArray[3] == '-' && emergencyPhoneArray[7] == '-'
                    && checkDigit(emergencyPhoneArray[0]) && checkDigit(emergencyPhoneArray[1])
                    && checkDigit(emergencyPhoneArray[2]) && checkDigit(emergencyPhoneArray[4])
                    && checkDigit(emergencyPhoneArray[5]) && checkDigit(emergencyPhoneArray[6])
                    && checkDigit(emergencyPhoneArray[8]) && checkDigit(emergencyPhoneArray[9])
                    && checkDigit(emergencyPhoneArray[10]) && checkDigit(emergencyPhoneArray[11])
        ) {
            return false; //the fields are in the correct format
        }
        return true; //the fields are not in the correct format


    }

    private boolean notEmailFormat(String Email, String Emergency_Email) {

        if(Email.contains("@") && Emergency_Email.contains("@")){
            return false; //the fields are in the correct format
        }
        return true; //the fields are not in the correct format

    }

    private boolean checkDigit(char digit) {
        return Character.isDigit(digit);
    }

}

