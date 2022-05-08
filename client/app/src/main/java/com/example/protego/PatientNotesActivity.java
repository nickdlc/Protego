package com.example.protego;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.protego.util.RandomGenerator;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.protego.web.schemas.Note;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Source;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientNotesActivity extends FragmentActivity
        implements NewNoteFragment.NoticeDialogListener {
    public static List<NotesInfo> notesData;
    public static final String TAG = "PatientNotesActivity";
    private FirebaseAuth mAuth;
    private LinearLayout layout;
    public static String userID;
    String visibility = "Public";
    private SwipeRefreshLayout swipeContainer;

    public static class NotesInfo {
        private final String note_id;
        private final String title;
        private final String date;
        //private final boolean visibility;
        private final String visibility;
        private final String details;


        public NotesInfo(String note_id, String title, String date, String visibility, String details) {
            this.note_id = note_id;
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
            return this.visibility;
        }

        public String getDetails() {
            return this.details;
        }

        public String getId() {
            return this.note_id;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_notes);
        notesData = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        PatientNotesActivity thisObj = this;
        final PatientNotesRecyclerViewAdapter adapter = new PatientNotesRecyclerViewAdapter(thisObj,notesData);

        userID = mAuth.getCurrentUser().getUid();

        //adapter.clear();
        FirestoreAPI.getInstance().getNotes(userID, new FirestoreListener<List<Note>>() {
            @Override
            public void getResult(List<Note> notes) {
                String note_id;
                String title;
                String date;
                String visibility;
                String details;

                RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

                for(Note note : notes) {
                    note_id = note.getNoteID();
                    title = note.getTitle();
                    date = note.getDateCreated().toString();
                    visibility = note.getVisibility();
                    details = note.getContent();

                    notesData.add(new NotesInfo(note_id, title,date,visibility,details));
                }

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));

            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //getPatientNotes(userID);
                adapter.clear();
                FirestoreAPI.getInstance().getNotes(userID, new FirestoreListener<List<Note>>() {
                    @Override
                    public void getResult(List<Note> notes) {
                        String note_id;
                        String title;
                        String date;
                        String visibility;
                        String details;

                        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

                        for(Note note : notes) {
                            note_id = note.getNoteID();
                            title = note.getTitle();
                            date = note.getDateCreated().toString();
                            visibility = note.getVisibility();
                            details = note.getContent();

                            notesData.add(new NotesInfo(note_id, title,date,visibility,details));
                        }

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(thisObj));

                        //adapter.addAll(notesData);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void getError(Exception e, String msg) {
                        Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                        Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
                    }
                });
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        findViewById(R.id.patientNotesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotes();
            }
        });



    }

    private void createNote(String uid, String title, String content, String visibility){
        FirestoreAPI.getInstance().createNote(uid, RandomGenerator.randomApprovedDoctors(), title, content, visibility, new FirestoreListener<Task>() {

            @Override
            public void getResult(Task object) {
                // do nothing, just generate data
                DocumentReference result = (DocumentReference) object.getResult();
                String ID = result.getId();
                result.update("noteID", ID); //will update the document's ID field
                Log.d(TAG, "Added note " + ID);
                Toast.makeText(
                        getApplicationContext(),
                        "Successfully added new note. Please swipe up to refresh.",
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
            }
        });

    }

/*
    private void getPatientNotes(String puid) {

    }*/


    private void addNotes(){
        DialogFragment fragment = new NewNoteFragment();
        fragment.show(getSupportFragmentManager(), "addNotes");
    }

    //allows the activity to create a new note when the user selects the "add" button on the dialog
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String title = NewNoteFragment.note_title;
        String content = NewNoteFragment.note_content;

        if(NewNoteFragment.visibility == true && NewNoteFragment.isVisibilitySelected){ //add the public note
             visibility = "Public";
        }

        else if(NewNoteFragment.visibility == false && NewNoteFragment.isVisibilitySelected) { //add a private note
             visibility = "Private";

        }

        if(title != null && content != null && NewNoteFragment.isVisibilitySelected) {
            createNote(userID, title, content, visibility);
            dialog.dismiss();

        } else {
            addNotes(); //creates a new note dialog when a user does not complete the note title and content fields
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

}
