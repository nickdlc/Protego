package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.Note;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewPatientNotes extends AppCompatActivity {

    private Button button;
    public static List<Note> notesData;
    public static final String TAG = "DoctorViewPatientNotes";
    private TextView tvFullName;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_notes);

        //setUpPatientNotes();
        connectButtonToActivity(R.id.returnFromNotes, DoctorViewPatientSelections.class);
        DoctorViewPatientNotes thisObj = this;
        Bundle extras = getIntent().getExtras();
        pid = extras.getString("pid");
        //System.out.println("Passing through " + pid);

        notesData = new ArrayList<>();

        FirestoreAPI.getInstance().getNotes(pid, new FirestoreListener<List<Note>>() {
            @Override
            public void getResult(List<Note> nList) {
                System.out.println("Notes List : " + nList);

                RecyclerView rvNotesForDoctors = findViewById(R.id.doctorViewPatientNotesRecyclerView);

                tvFullName = findViewById(R.id.notesPatientFullNameInput);
                tvFullName.setText(extras.getString("patientFullName"));

                for(Note note : nList){
                    notesData.add(new Note(note.getTitle(), note.getDateCreated(), note.getVisibility(), note.getContent()));
                    Log.d(TAG, "info : " + note.getTitle());
                    Log.d(TAG, "info : " + note.getDateCreated().toString());
                    Log.d(TAG, "info : " + note.getVisibility());
                    Log.d(TAG, "info : " + note.getContent());
                }

                // create adapter
                final PatientNotesRecyclerViewAdapter adapter = new PatientNotesRecyclerViewAdapter(thisObj, notesData);
                // Set the adapter on recyclerview
                rvNotesForDoctors.setAdapter(adapter);
                // set a layout manager on RV
                rvNotesForDoctors.setLayoutManager(new LinearLayoutManager(thisObj));
                Log.d(TAG, "Received request for patients' notes data");
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get notes:\n\t" + msg, e);
                Toast.makeText(DoctorViewPatientNotes.this, msg, Toast.LENGTH_LONG);
            }
        });
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