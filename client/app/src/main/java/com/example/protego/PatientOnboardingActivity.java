package com.example.protego;

import static com.example.protego.MainActivity.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.tasks.Task;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PatientOnboardingActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up google sign to ask for basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // Sets first time in dashboard to false so
        // user doesn't get taken back to onboarding when going to dashboard
        PatientDashboardActivity.firstTime = false;

        setContentView(R.layout.patient_onboarding);
        connectButtonToActivity(R.id.skipForNowBtn,PatientDashboardActivity.class);
        connectButtonToGoogleFit(R.id.connectFitButton);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("sign-in", "signInResult:failed code=" + e.getStatusCode());
        }
    }
//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//    }

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

    private void connectButtonToGoogleFit(Integer buttonId) {
        Button button;
        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                GoogleSignInOptionsExtension fitnessOptions = FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .build();

                // Read the data that's been collected throughout the past week.
                ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
                ZonedDateTime startTime = endTime.minusWeeks(1);
                Log.i(TAG, "Range Start: $startTime");
                Log.i(TAG, "Range End: $endTime");

                DataReadRequest readRequest = new DataReadRequest.Builder()
                        .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
                        .bucketByActivityType(1, TimeUnit.DAYS)
                        .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                        .build();

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);

                account = GoogleSignIn.getAccountForExtension(PatientOnboardingActivity.this, fitnessOptions);

                // Check Permission for Access Fine Location
                int permissionCheck = ContextCompat.checkSelfPermission(PatientOnboardingActivity.this,
                        Manifest.permission.ACTIVITY_RECOGNITION);
                if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    // ask permissions here using below code
                    ActivityCompat.requestPermissions(PatientOnboardingActivity.this,
                            new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                            REQUEST_CODE);
                }



                Fitness.getHistoryClient(PatientOnboardingActivity.this, account)
                        .readData(readRequest)
                        .addOnSuccessListener (response -> {
                            // The aggregate query puts datasets into buckets, so convert to a
                            // single list of datasets
                            Toast.makeText(PatientOnboardingActivity.this, "Did work", Toast.LENGTH_SHORT).show();
                            Log.d("Fit", "Reading Success!");
                            for (Bucket bucket : response.getBuckets()) {
                                for (DataSet dataSet : bucket.getDataSets()) {
                                    dumpDataSet(dataSet);
                                }
                            }
                        })
                        .addOnFailureListener(e ->{
                            Toast.makeText(PatientOnboardingActivity.this, "Did not work", Toast.LENGTH_SHORT).show();
                                Log.w("Fit", "There was an error reading data from Google Fit", e);
                        });

            }

            private void dumpDataSet(DataSet dataSet) {
                Log.i("Fit", "Data returned for Data type: ${dataSet.dataType.name}");
                for (DataPoint dp : dataSet.getDataPoints()) {
                    Log.i("Fit","Data point:");
                    Log.i("Fit","\tType: ${dp.dataType.name}");
                    Log.i("Fit","\tStart: ${dp.getStartTimeString()}");
                    Log.i("Fit","\tEnd: ${dp.getEndTimeString()}");
                    for (Field field : dp.getDataType().getFields()) {
                        Log.i("Fit","\tField: ${field.name.toString()} Value: ${dp.getValue(field)}");
                    }
                }
            }

        });
    }

    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}");
        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG,"Data point:");
            Log.i(TAG,"\tType: ${dp.dataType.name}");
            Log.i(TAG,"\tStart: ${dp.getStartTimeString()}");
            Log.i(TAG,"\tEnd: ${dp.getEndTimeString()}");
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG,"\tField: ${field.name.toString()} Value: ${dp.getValue(field)}");
            }
        }
    }
}

