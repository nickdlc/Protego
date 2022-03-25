package com.example.protego;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientNotesActivity extends AppCompatActivity {
    public static class NotesInfo {
        private final String title;
        private final String date;
        private final boolean visibility;
        private final String details;

        public NotesInfo(String title, String date, boolean visibility, String details) {
            this.title = title;
            this.date = date;
            this.visibility = visibility;
            this.details = details;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getVisibility() {
            if(visibility) return "visibility: public";
            else return "visibility: private";
        }

        public String getDetails() {
            return details;
        }
    }

    ArrayList<NotesInfo> notesData = new ArrayList<>();

    private void setUpPatientNotes(){
        notesData.add(new NotesInfo("test 1","2/24/2022",true,"This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022",true,"This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022",true,"This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022",true,"This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022",true,"This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022",true,"This is a test note"));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_notes);

        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

        setUpPatientNotes();

        PatientNotesRecyclerViewAdapter adapter = new PatientNotesRecyclerViewAdapter(this,notesData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
