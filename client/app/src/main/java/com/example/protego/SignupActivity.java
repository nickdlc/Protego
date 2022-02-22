package com.example.protego;

import android.app.Fragment;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;

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

        //mAuth = FirebaseAuth.getInstance();

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
/*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }*/

    private void registerUser(String email, String password) {
        Log.i(TAG, "Attempting to register user " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            goMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

    // navigate to the main activity once the user has signed up
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class); // TODO: change to user dashboard activity 
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
        Intent intent = new Intent(this, LoginActivity.class); //To do: change the loginActivity to the patient Dashboard Activity

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
        startActivity(intent); //starts an instance of the doctor dashboard

    }

    public void doctor_Dashboard_Screen(View view){
        Intent intent = new Intent(this, MainActivity.class); //To do: change the loginActivity to the doctor Dashboard Activity

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
        startActivity(intent); //starts an instance of the doctor dashboard

    }




}


