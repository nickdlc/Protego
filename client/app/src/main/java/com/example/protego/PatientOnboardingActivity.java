package com.example.protego;

import static com.example.protego.MainActivity.TAG;

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
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class PatientOnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets first time in dashboard to false so
        // user doesn't get taken back to onboarding when going to dashboard
        PatientDashboardActivity.firstTime = false;

        setContentView(R.layout.patient_onboarding);
        connectButtonToActivity(R.id.skipForNowBtn,PatientDashboardActivity.class);
        connectButtonToGoogleFit(R.id.connectFitButton);
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

    private void connectButtonToGoogleFit(Integer buttonId) {
        Button button;
        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                FitnessOptions fitnessOptions = FitnessOptions.builder()
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
                        .bucketByActivityType(7, TimeUnit.DAYS)
                        .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                        .build();

                Fitness.getHistoryClient(PatientOnboardingActivity.this, GoogleSignIn.getAccountForExtension(PatientOnboardingActivity.this, fitnessOptions))
                        .readData(readRequest)
                        .addOnSuccessListener (response -> {
                            // The aggregate query puts datasets into buckets, so convert to a
                            // single list of datasets
                            Toast.makeText(PatientOnboardingActivity.this, "Did work", Toast.LENGTH_SHORT).show();
                            for (Bucket bucket : response.getBuckets()) {
                                for (DataSet dataSet : bucket.getDataSets()) {
                                    dumpDataSet(dataSet);
                                }
                            }
                        })
                        .addOnFailureListener(e ->{
                            Toast.makeText(PatientOnboardingActivity.this, "Did not work", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "There was an error reading data from Google Fit", e);
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

