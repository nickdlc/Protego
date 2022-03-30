package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientNotesActivity extends AppCompatActivity {
    public static ArrayList<NotesInfo> notesData = new ArrayList<>();
    public static final String TAG = "PatientNotesActivity";
    private FirebaseAuth mAuth;


    public static class NotesInfo {
        private final String title;
        private final String date;
        //private final boolean visibility;
        private final String visibility;
        private final String details;


        public NotesInfo(String title, String date, String visibility, String details) {
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
//            if(visibility) return "visibility: public";
//            else return "visibility: private";
            return this.visibility;
        }

        public String getDetails() {
            return details;
        }
    }


    private void setUpPatientNotes(){
        notesData.add(new NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("test 1","2/24/2022","Public","This is a test note"));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_notes);

        mAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

        //setUpPatientNotes();

        PatientNotesRecyclerViewAdapter adapter = new PatientNotesRecyclerViewAdapter(this,notesData);
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

                finish();
            }
        });
    }


    private void createNote(String uid){

        ServerAPI.generateNoteData(uid, new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                // do nothing, just generate data
            }
            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

    }



    private void getPatientNotes(String puid) {
        ServerAPI.getNotes(puid, new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                try {
                    JSONArray res = req.getResultJSONList();

                    String title;
                    String date;
                    String visibility;
                    String details;


                    for(int i = 0; i < res.length(); i++) {

                        JSONObject object = res.getJSONObject(i);

                        title = object.getString("title");
                        date = object.getString("dateCreated");
                        visibility = object.getString("visibility");
                        details = object.getString("content");

                        notesData.add(new NotesInfo(title,date,visibility,details));

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Could not get JSON from request : ", e);
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
            }



        });
    }

}
