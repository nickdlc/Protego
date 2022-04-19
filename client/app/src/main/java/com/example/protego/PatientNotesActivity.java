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
import java.util.List;

public class PatientNotesActivity extends FragmentActivity
        implements NewNoteFragment.NoticeDialogListener {
    public static ArrayList<NotesInfo> notesData = new ArrayList<>();
    public static final String TAG = "PatientNotesActivity";
    private FirebaseAuth mAuth;
    private LinearLayout layout;
    public static String userID;
    String visibility = "Public";



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


    private void setUpPatientNotes(){
        notesData.add(new NotesInfo("1", "test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("2", "test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("3", "test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("4", "test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("5", "test 1","2/24/2022","Public","This is a test note"));
        notesData.add(new NotesInfo("6", "test 1","2/24/2022","Public","This is a test note"));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_notes);

        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        RecyclerView recyclerView = findViewById(R.id.patientMedicationRecyclerView);

        PatientNotesRecyclerViewAdapter adapter = new PatientNotesRecyclerViewAdapter(this,notesData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                String note_id;
                String title;
                String date;
                String visibility;
                String details;


                for(Note note : notes) {
                    note_id = note.getNoteID();
                    title = note.getTitle();
                    date = note.getDateCreated().toString();
                    visibility = note.getVisibility();
                    details = note.getContent();

                    notesData.add(new NotesInfo(note_id, title,date,visibility,details));

                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "Failed to get vitals for patient:\n\t" + msg, e);
                Toast.makeText(PatientNotesActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }


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

            Intent i = new Intent(this, PatientDashboardActivity.class);
            startActivity(i);

        } else {
            addNotes(); //creates a new note dialog when a user does not complete the note title and content fields
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

}
