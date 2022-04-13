package com.example.protego;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.protego.util.RandomGenerator;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.provider.FirebaseInitProvider;

import java.util.List;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String TAG = "SignupActivity";
    public boolean isAuth = false;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    // add other input fields here
    private Button btnSignup;
    private EditText first_name_input;
    private EditText last_name_input;
    private EditText email_input;
    private EditText password_input;
    private EditText confirm_password_input;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //the spinner component
        spinner = (Spinner) findViewById(R.id.typeOfUserSpinner);
        //ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_of_user_array, android.R.layout.simple_spinner_item);
        //specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        
        /*btnSignup = findViewById(R.id.btnSignup);
            btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");

                String email = email_input.getText().toString();
                String password = password_input.getText().toString();
                registerUser(email, password);
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void registerUser(String email, String password) {
        Log.i(TAG, "Attempting to register user " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If sign in fails, display a message to the user.
                        // TODO: Show 5xx server error to user
                        Log.w(TAG, "registerUser:failure", e);
                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
               })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Task completed successfully
                        // ...
                        // Sign in success, update UI with the signed-in user's information
                        isAuth = true;
                        Log.d(TAG, "registerUser:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        Log.d(TAG, "Creating the user...");

                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        ProtegoUser protegoUser = new ProtegoUser();
                        protegoUser.setFirstName(first_name_input.getText().toString());

                        if (last_name_input != null) {
                            // is a doctor, set last name
                            protegoUser.setLastName(last_name_input.getText().toString());
                            protegoUser.setUserType(ProtegoUser.ProtegoUserType.DOCTOR);
                        } else {
                            // is a patient
                            protegoUser.setUserType(ProtegoUser.ProtegoUserType.PATIENT);
                        }

                        String uid = firebaseUser.getUid();
                        firestore.collection("users").document(uid)
                                .set(protegoUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Successfully created user " + uid);
                                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                            Toast.makeText(SignupActivity.this, "Check your email for a verification link.", Toast.LENGTH_LONG);
                                            List<String> randomDoctors = RandomGenerator.randomApprovedDoctors();
                                            RandomGenerator.randomApprovedDoctors = randomDoctors;

                                            firebaseUser.sendEmailVerification();

                                            if (last_name_input == null) {

                                                FirestoreAPI.getInstance().generateMedData(uid, new FirestoreListener() {
                                                    @Override
                                                    public void getResult(Object object) {
                                                        // Do nothing
                                                    }

                                                    @Override
                                                    public void getError(Exception e, String msg) {
                                                        Log.e(TAG, "Failed to generate medical information data:\n\t" + msg, e);
                                                        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG);
                                                    }
                                                });

                                                // generate random vital information
                                                FirestoreAPI.getInstance().generateVitalData(uid, new FirestoreListener<Task>() {
                                                    @Override
                                                    public void getResult(Task object) {
                                                        // do nothing, just generate data
                                                    }

                                                    @Override
                                                    public void getError(Exception e, String msg) {
                                                        Log.e(TAG, "Failed to generate vital data:\n\t" + msg, e);
                                                        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG);
                                                    }
                                                });

                                                //generate random Note information
                                                FirestoreAPI.getInstance().generateNoteData(uid, randomDoctors, new FirestoreListener<Task>() {
                                                    @Override
                                                    public void getResult(Task object) {
                                                        // Do nothing
                                                    }

                                                    @Override
                                                    public void getError(Exception e, String msg) {
                                                        Log.e(TAG, "Failed to generate note data:\n\t" + msg, e);
                                                        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG);
                                                    }
                                                });

                                                //generate random Medication information
                                                FirestoreAPI.getInstance().generateMedicationData(uid, randomDoctors, new FirestoreListener() {
                                                    @Override
                                                    public void getResult(Object object) {
                                                        // Do nothing
                                                    }

                                                    @Override
                                                    public void getError(Exception e, String msg) {
                                                        Log.e(TAG, "Failed to generate medication data:\n\t" + msg, e);
                                                        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG);
                                                    }
                                                });
                                            }
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing new user to Firestore", e);
                                            // redirect to the MainActivity page
                                            goLoginActivity();
                                        }
                                    });
                    }
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

    // navigate to the main activity once the user has signed up
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userType = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();
        String[] userTypeOptions = resource.getStringArray(R.array.type_of_user_array);
        FragmentContainerView patient_view = (FragmentContainerView) findViewById(R.id.patientFragmentContainerView);
        FragmentContainerView doctor_view = (FragmentContainerView) findViewById(R.id.doctorFragmentContainerView);


        if(userType.equals(userTypeOptions[0])){ //the user is a patient therefore only the patient signup fragment is visible
            patient_view.setVisibility(FragmentContainerView.INVISIBLE);
            doctor_view.setVisibility(FragmentContainerView.INVISIBLE);
        }

        else if(userType.equals(userTypeOptions[1])){ //the user is a patient therefore only the patient signup fragment is visible
            patient_view.setVisibility(FragmentContainerView.VISIBLE);
            doctor_view.setVisibility(FragmentContainerView.INVISIBLE);
            btnSignup = (Button) findViewById(R.id.patientSignupButton);
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    patient_Dashboard_Screen(v); //to view the Patient Dashboard when the sign up button is clicked
                }
            });

        } // TODO: save user type during signup process as well so we know which dashboard to display when they login

        else if(userType.equals(userTypeOptions[2])) { // the user is a doctor therefore only the doctor signup fragment is visible
            patient_view.setVisibility(FragmentContainerView.INVISIBLE);
            doctor_view.setVisibility(FragmentContainerView.VISIBLE);
            btnSignup = (Button) findViewById(R.id.doctorSignupButton);
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    doctor_Dashboard_Screen(v);  //to view the Doctor Dashboard when the sign up button is clicked
                }
            });
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void patient_Dashboard_Screen(View view){
        Intent intent = new Intent(this, MainActivity.class);
        first_name_input = (TextInputEditText) findViewById(R.id.patientFirstNameTextInput);
        email_input = (TextInputEditText) findViewById(R.id.patientEmailTextInput);
        password_input = (TextInputEditText) findViewById(R.id.patientPasswordTextInput);
        confirm_password_input = (TextInputEditText) findViewById(R.id.patientConfirmPasswordTextInput);

        //To do: Add constraints to make sure the Patient completed all sign-up fields

        String first_name = first_name_input.getText().toString();
        String email = email_input.getText().toString();
        String password = password_input.getText().toString();
        String confirm_password = confirm_password_input.getText().toString();

        registerUser(email, password);

        Bundle signup_bundle = new Bundle();
        signup_bundle.putString(first_name, first_name);
        signup_bundle.putString(email, email);
        signup_bundle.putString(password, password);
        signup_bundle.putString(confirm_password, confirm_password);

        intent.putExtras(signup_bundle); //sets the bundle to the intent

        if(isAuth)
            startActivity(intent); //starts an instance of the patient dashboard
        }

    public void doctor_Dashboard_Screen(View view){
        Intent intent = new Intent(this, DoctorDashboardActivity.class);

        first_name_input = (TextInputEditText) findViewById(R.id.doctorFirstNameTextInput);
        last_name_input = (TextInputEditText) findViewById(R.id.doctorLastNameTextInput);
        email_input = (TextInputEditText) findViewById(R.id.doctorEmailTextInput);
        password_input = (TextInputEditText) findViewById(R.id.doctorPasswordTextInput);
        confirm_password_input = (TextInputEditText) findViewById(R.id.doctorConfirmPasswordTextInput);

        //To do: Add constraints to make sure the doctor completed all sign up-fields

        String first_name = first_name_input.getText().toString();
        String last_name = last_name_input.getText().toString();
        String email = email_input.getText().toString();
        String password = password_input.getText().toString();
        String confirm_password = confirm_password_input.getText().toString();

        registerUser(email, password);

        Bundle signup_bundle = new Bundle();
        signup_bundle.putString(first_name, first_name); //The first String is the key and the second string is the String associated to the key
        signup_bundle.putString(last_name, last_name);
        signup_bundle.putString(email, email);
        signup_bundle.putString(password, password);
        signup_bundle.putString(confirm_password, confirm_password);

        intent.putExtras(signup_bundle); //sets the bundle to the intent

        if(isAuth)
            startActivity(intent); //starts an instance of the doctor dashboard

    }




}


