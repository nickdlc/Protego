package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.util.RandomGenerator;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.Note;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientNotesActivity extends AppCompatActivity {
    public static List<Note> notesData;
    public static final String TAG = "PatientNotesActivity";
    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_notes);
        notesData = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

        //setUpPatientNotes();

        PatientNotesRecyclerViewAdapter adapter = new PatientNotesRecyclerViewAdapter(this, notesData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.patientNotesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote(mAuth.getUid());
                notesData.clear();
                recreate();
                Intent i = new Intent(v.getContext(), PatientDashboardActivity.class);
                startActivity(i);

                //finish();
            }
        });
    }


    private void createNote(String uid){
        FirestoreAPI.getInstance().generateNoteData(uid, RandomGenerator.randomApprovedDoctors(), new FirestoreListener<Task>() {
            @Override
            public void getResult(Task object) {
                // do nothing, just generate data
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }



    private void getPatientNotes(String puid) {
        FirestoreAPI.getInstance().getNotes(puid, new FirestoreListener<List<Note>>() {
            @Override
            public void getResult(List<Note> notes) {
                String title;
                Date date;
                String visibility;
                String details;


                for(Note note : notes) {
                    title = note.getTitle();
                    date = note.getDateCreated();
                    visibility = note.getVisibility();
                    details = note.getContent();

                    notesData.add(new Note(title,date,visibility,details));

                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

}
