package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DoctorViewPatientNotes extends AppCompatActivity {

    private Button button;
    public static ArrayList<PatientNotesActivity.NotesInfo> notesData = PatientNotesActivity.notesData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_notes);
        RecyclerView recyclerView = findViewById(R.id.doctorViewPatientNotesRecyclerView);

        setUpPatientNotes();

        connectButtonToActivity(R.id.returnFromNotes, DoctorViewPatientSelections.class);


        PatientNotesRecyclerViewAdapter adapter = new PatientNotesRecyclerViewAdapter(this,notesData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setUpPatientNotes(){
        notesData.clear();
        notesData.add(new PatientNotesActivity.NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new PatientNotesActivity.NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new PatientNotesActivity.NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new PatientNotesActivity.NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new PatientNotesActivity.NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new PatientNotesActivity.NotesInfo("test 1","2/24/2022","Public","This is a test note"));
    }


    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

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


}