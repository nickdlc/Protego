package com.example.protego.dashboard.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.R;
import com.example.protego.dashboard.notification.menu.note.PatientNotesActivity;
import com.example.protego.web.firestore.FirestoreAPI;
import com.example.protego.web.firestore.FirestoreListener;
import com.example.protego.web.schemas.firestore.Note;

import java.util.ArrayList;
import java.util.List;

public class DoctorViewPatientNotes extends AppCompatActivity {

    private Button button;
    public static List<PatientNotesActivity.NotesInfo> notesData;
    public static final String TAG = "DoctorViewPatientNotes";
    private TextView tvFullName;
    private String pid;
    private String patientFirst;
    private String patientLast;
    private String name;
    private String onboardingFlag;
    private TextView no_notes_message;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_patient_notes);

        //setUpPatientNotes();
        connectButtonToActivity(R.id.returnFromNotes, DoctorViewPatientSelections.class);
        DoctorViewPatientNotes thisObj = this;
        Bundle extras = getIntent().getExtras();
        patientFirst = extras.getString("patientFirst");
        patientLast = extras.getString("patientLast");
        name = patientFirst + " " + patientLast;
        pid = extras.getString("patientId");
        onboardingFlag = extras.getString("onboardingFlag");
        no_notes_message = (TextView) findViewById(R.id.noNotesTextView);
        //System.out.println("Passing through " + pid);

        tvFullName = findViewById(R.id.notesPatientFullNameInput);
        tvFullName.setText(name);

        notesData = new ArrayList<>();

        FirestoreAPI.getInstance().getPublicNotes(pid, new FirestoreListener<List<Note>>() {
            @Override
            public void getResult(List<Note> nList) {
                String note_id;
                String title;
                String date;
                String visibility;
                String details;

                System.out.println("Notes List : " + nList);

                RecyclerView rvNotesForDoctors = findViewById(R.id.doctorViewPatientNotesRecyclerView);

                for(Note note : nList){
                    note_id = note.getNoteID();
                    title = note.getTitle();
                    date = note.getDateCreated().toString();
                    visibility = note.getVisibility();
                    details = note.getContent();

                    notesData.add(new PatientNotesActivity.NotesInfo(note_id, title, date, visibility, details));
                    Log.d(TAG, "info : " + note.getTitle());
                    Log.d(TAG, "info : " + note.getDateCreated().toString());
                    Log.d(TAG, "info : " + note.getVisibility());
                    Log.d(TAG, "info : " + note.getContent());
                }


                if(!notesData.isEmpty()) {
                    no_notes_message.setVisibility(View.GONE);
                    // create adapter
                    final DoctorViewPatientNotesAdapter adapter = new DoctorViewPatientNotesAdapter(thisObj, notesData);
                    // Set the adapter on recyclerview
                    rvNotesForDoctors.setAdapter(adapter);
                    // set a layout manager on RV
                    rvNotesForDoctors.setLayoutManager(new LinearLayoutManager(thisObj));
                } else { //when the user has no public notes then show text saying so
                    no_notes_message.setVisibility(View.VISIBLE);
                }

                Log.d(TAG, "Received request for patients' notes data");
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get notes:\n\t" + msg, e);
                Toast.makeText(DoctorViewPatientNotes.this, msg, Toast.LENGTH_LONG);
            }
        });

        final DoctorViewPatientNotesAdapter adapter = new DoctorViewPatientNotesAdapter(thisObj, notesData);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.doctorViewNoteSwipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getPatientNotes(userID);
                adapter.clear();
                FirestoreAPI.getInstance().getPublicNotes(pid, new FirestoreListener<List<Note>>() {
                    @Override
                    public void getResult(List<Note> notes) {
                        String note_id;
                        String title;
                        String date;
                        String visibility;
                        String details;

                        RecyclerView recyclerView = findViewById(R.id.doctorViewPatientNotesRecyclerView);

                        for(Note note : notes) {
                            note_id = note.getNoteID();
                            title = note.getTitle();
                            date = note.getDateCreated().toString();
                            visibility = note.getVisibility();
                            details = note.getContent();

                            notesData.add(new PatientNotesActivity.NotesInfo(note_id, title,date,visibility,details));
                        }


                        if(!notesData.isEmpty()) {
                            no_notes_message.setVisibility(View.GONE);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));

                        } else { //when the user has no public notes then show text saying so
                            no_notes_message.setVisibility(View.VISIBLE);
                        }


                        //adapter.addAll(notesData);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void getError(Exception e, String msg) {
                        Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                    }
                });
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                i.putExtra("patientFirst", patientFirst);
                i.putExtra("patientLast", patientLast);
                i.putExtra("patientId", pid);
                i.putExtra("onboardingFlag", onboardingFlag);
                startActivity(i);
                finish();
            }
        });
    }


}