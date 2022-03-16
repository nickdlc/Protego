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

import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;


import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity{
    public static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Spinner spinner;
    private Button btnForgotPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
/*
        //the spinner component to determine the type of user - patient or doctor
        spinner = (Spinner) findViewById(R.id.loginTypeOfUserSpinner);
        //ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_of_user_array, android.R.layout.simple_spinner_item);
        //specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
*/

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

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
                            ProtegoUser protegoUser;
                            //ProtegoUser protegoUser = new ProtegoUser(mAuth.getUid());
                            //Log.d(TAG, protegoUser.toString());
                            String uid = user.getUid();

                            if (user.isEmailVerified()) {
                                updateUI(user);
                                DocumentReference docRef = firestore.collection("users").document(uid);
                                docRef.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document != null) {
                                            Log.i(TAG,"userType "+document.getString("userType"));
                                            if(document.getString("userType").equals("DOCTOR"))
                                                connectButtonToActivity(R.id.btnLogin, DoctorDashboardActivity.class);
                                            else
                                                connectButtonToActivity(R.id.btnLogin, PatientDashboardActivity.class);

                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "failed with ", task1.getException());
                                    }
                                });

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

    // navigate to the appropriate dashboard activity if the user has signed in properly
    private void goDoctorActivity() {
        Intent i = new Intent(this, DoctorDashboardActivity.class);
        startActivity(i);
        finish();
    }



    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    /*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userType = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();
        String[] userTypeOptions = resource.getStringArray(R.array.type_of_user_array);

        //TODO: check that the user first login with the correct credentials and user type if so then their user type selection determines their dashboard view
        if(userType.equals(userTypeOptions[1])){ //the user is a patient therefore the patient dashboard is shown
            connectButtonToActivity(R.id.btnLogin, PatientDashboardActivity.class);

        }
        else if(userType.equals(userTypeOptions[2])) { // the user is a doctor therefore the doctor dashboard is shown
            connectButtonToActivity(R.id.btnLogin, DoctorDashboardActivity.class);
            //isPatient = false;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }*/

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
            }
        });
    }
}