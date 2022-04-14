package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Doctor;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class PatientViewDoctorsActivity extends AppCompatActivity {
    public static final String TAG = "PatientViewDoctorsActivity";

    private Button btnReturn;
    private ArrayList<Doctor> doctors;
    private RecyclerView rvDoctors;
    private DoctorsListAdapter.RecyclerViewClickListener listener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_view_doctors);

        mAuth = FirebaseAuth.getInstance();
        PatientViewDoctorsActivity thisObj = this;

        btnReturn = findViewById(R.id.PatientViewDoctorsReturnButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        String puid = mAuth.getCurrentUser().getUid();

        FirestoreAPI.getInstance().getPatientsDoctors(puid, new FirestoreListener<List<Doctor>>() {
            @Override
            public void getResult(List<Doctor> object) {
                doctors = new ArrayList<>(object);
                setRecyclerView();
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get doctors\n\t" + msg, e);
            }
        });
    }

    private void setRecyclerView() {
        rvDoctors = findViewById(R.id.rvDoctors);

        // Set the onClick listener for each element in the RecyclerView
        setOnClickListener();

        DoctorsListAdapter adapter = new DoctorsListAdapter(doctors, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvDoctors.setLayoutManager(layoutManager);
        rvDoctors.setItemAnimator(new DefaultItemAnimator());
        rvDoctors.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new DoctorsListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), PatientViewDoctorProfile.class);
                i.putExtra("firstName", doctors.get(position).getFirstName());
                i.putExtra("lastName", doctors.get(position).getLastName());
                startActivity(i);
            }
        };
    }
}