package com.example.protego;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Spinner spinner;
    private Button buttonLogin;
    private Button btnForgotPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //the spinner component to determine the type of user - patient or doctor
        spinner = (Spinner) findViewById(R.id.loginTypeOfUserSpinner);
        //ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_of_user_array, android.R.layout.simple_spinner_item);
        //specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(email, password);
            }
        });

        //connects the button for users who do not have accounts to the sign up activity.
        connectButtonToActivity(R.id.btnSignup, SignupActivity.class);

        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick forgot password button");
                Intent i = new Intent(v.getContext(), ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnSignUp = findViewById(R.id.btnSignup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick register here button");
                Intent i = new Intent(v.getContext(), SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
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

    private void loginUser(String email, String password) {
        Log.i(TAG, "Attempting to login user " + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user.isEmailVerified()) {
                                updateUI(user);
                                goMainActivity();
                            } else {
                                Log.d(TAG, "user not verified by email");
                                Toast.makeText(LoginActivity.this, "Please verify your email with the sent link.", Toast.LENGTH_LONG);
                                // TODO: have a button that says "resend link"?
                                // to resend link, call `user.sendEmailVerification();`
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

    // navigate to the main activity if the user has signed in properly
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userType = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();
        String[] userTypeOptions = resource.getStringArray(R.array.type_of_user_array);

        //TODO: check that the user first login with the correct credentials and user type if so then their user type selection determines their dashboard view
        if(userType.equals(userTypeOptions[1])){ //the user is a patient therefore the patient dashboard is shown
            //TODO: Change Signup Activity to Doctor Activity once the Activity is on the branch
            connectButtonToActivity(R.id.btnLogin, SignupActivity.class); //Temporarily connects to Signup Activity to Test
            //connectButtonToActivity(R.id.btnLogin, PatientDashboardActivity.class);

        }
        else if(userType.equals(userTypeOptions[2])) { // the user is a doctor therefore the doctor dashboard is shown
            connectButtonToActivity(R.id.btnLogin, DoctorDashboardActivity.class);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        Button button = findViewById(buttonId);
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