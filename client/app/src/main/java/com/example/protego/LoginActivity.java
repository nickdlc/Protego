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

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.RequestManager;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.ProtegoUser;
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
    public static String userT;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Spinner spinner;
    private Button btnForgotPassword;
    private Button btnSignUp;

    public static String flagData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up Volley if external API calls are enabled
        if (RequestManager.ACTIVE)
            RequestManager.getInstance(this);

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
        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intentD = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
        Intent intentP = new Intent(LoginActivity.this, PatientDashboardActivity.class);*/

        //System.out.println(userT);
        reload();
        /*if(currentUser != null){
            //reload();
            System.out.println("userT = " + userT);
            if(userT.equals("DOCTOR"))
                startActivity(intentD);
            else if(userT.equals("PATIENT"))
                startActivity(intentP);
        }*/

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

                            String uid = user.getUid();

                            if (user.isEmailVerified()) {
                                //updateUI(user);
                                DocumentReference docRef = firestore.collection("users").document(uid);
                                docRef.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document != null) {
                                            Log.i(TAG,"userType "+document.getString("userType"));
                                          
                                            Intent intentD = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
                                            Intent intentP = new Intent(LoginActivity.this, PatientDashboardActivity.class);
                                            if(document.getString("userType").equals("DOCTOR")) {
                                                startActivity(intentD);
                                            }

                                            else {
                                               startActivity(intentP);
                                                //the goToOnboarding is included in this function to determine whether to show onboarding form
                                                getOnboardingFlag(uid);
                                            }
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

    private void updateUI(String userType) {
        userT = userType;
    }

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



    private void getOnboardingFlag(String id) {

        FirestoreAPI.getInstance().getOnboardingFlag(id, new FirestoreListener<DocumentSnapshot>() {
            @Override
            public void getResult(DocumentSnapshot object) {
                flagData = object.get("Onboarding Completed").toString();
                Log.v(TAG, "flag: " + flagData);
                goToOnboarding(id);
            }
            @Override
            public void getError(Exception e, String msg) {

            }
        });
    }


    // Function that handles going to onboarding if user is new
    private void goToOnboarding(String uid) {
        Log.v(TAG, "onboarding flag: "+ flagData);

        if(flagData == "false"){
            PatientOnboardingActivity.flag = "false";
            Intent i = new Intent(this, PatientOnboardingActivity.class);
            startActivity(i);
            finish();
        }else{
            PatientOnboardingActivity.flag = "true";
            Intent intentP = new Intent(LoginActivity.this, PatientDashboardActivity.class);
            startActivity(intentP);
        }
    }
}