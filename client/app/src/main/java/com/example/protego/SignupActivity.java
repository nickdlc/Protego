package com.example.protego;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String TAG = "SignupActivity";

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

        //the spinner component
        spinner = (Spinner) findViewById(R.id.typeOfUserSpinner);
        //ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_of_user_array, android.R.layout.simple_spinner_item);
        //specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        /*
        btnSignup = findViewById(R.id.btnSignup);
            btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");

                String username = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                registerUser(username, password);
            }
        });*/
    }

    private void registerUser(String username, String password) {
        Log.i(TAG, "Attempting to register user " + username);

        // ADD firebase signup here (Parse signup setup is shown)

        /*
        // Create the User
        ParseUser user = new ParseUser();

        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with registration", e);
                    Toast.makeText(LoginActivity.this, "Issue with registration!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                goMainActivity();
            }
        });
        */

        }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userType = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();
        String[] userTypeOptions = resource.getStringArray(R.array.type_of_user_array);
        FragmentContainerView patient_view = (FragmentContainerView) findViewById(R.id.patientFragmentContainerView);
        FragmentContainerView doctor_view = (FragmentContainerView) findViewById(R.id.doctorFragmentContainerView);


        if(userType.equals(userTypeOptions[0])){ //the user is a patient therefore only the patient signup fragment is visible
            patient_view.setVisibility(FragmentContainerView.VISIBLE);
            doctor_view.setVisibility(FragmentContainerView.INVISIBLE);
            btnSignup = (Button) findViewById(R.id.patientSignupButton);
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    patient_Dashboard_Screen(v); //to view the Patient Dashboard when the sign up button is clicked
                }
            });

        }

        else if(userType.equals(userTypeOptions[1])) { // the user is a doctor therefore only the doctor signup fragment is visible
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


