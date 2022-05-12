package com.example.protego;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.Note;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class PatientNotesRecyclerViewAdapter extends RecyclerView.Adapter<PatientNotesRecyclerViewAdapter.MyViewHolder> {
    Context context;

    List<PatientNotesActivity.NotesInfo> patientNotes;
    public static final String TAG = "PatientNotesRecyclerViewAdapter";

    public PatientNotesRecyclerViewAdapter(Context context, List<PatientNotesActivity.NotesInfo> patientNotes) {
        Log.d("PatientNotesAdapter", "patientNotesAdapter");
        this.context = context;
        this.patientNotes = patientNotes;
    }

    @NonNull
    @Override
    public PatientNotesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("PatientNotesAdapter", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_notes_note_container,parent,false);


        return new PatientNotesRecyclerViewAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull PatientNotesRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(patientNotes.get(position).getTitle());
        holder.tvDate.setText(patientNotes.get(position).getDate());
        holder.tvVisibility.setText(patientNotes.get(position).getVisibility());
        holder.tvDetails.setText(patientNotes.get(position).getDetails());

        View card = holder.itemView.findViewById(R.id.patientMedicationCard);

        if(patientNotes.get(position).getVisibility().equals("Private")){
            card.setBackgroundColor(context.getColor(R.color.protego_pink));
        }
        else if (patientNotes.get(position).getVisibility().equals("Public")){
            card.setBackgroundColor(context.getColor(R.color.protego_blue));
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PatientNotesActivity.NotesInfo note_item = patientNotes.get(holder.getAdapterPosition());


                //to store the ID of the note queried in the database which matches the ID of the note selected in the recycler view
                final String[] noteID_queried = {note_item.getId()};
                FirestoreAPI.getInstance().queryNote(PatientNotesActivity.userID, noteID_queried[0], new FirestoreListener<Task>() {

                    @Override
                    public void getResult(Task object) {
                        // do nothing, just generate data

                        Task<QuerySnapshot> task = object;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            noteID_queried[0] = document.getId();
                            Log.v(TAG, document.getId() + " => " + document.getData());

                        }
                    }

                    @Override
                    public void getError(Exception e, String msg) {

                    }
                    });


                try{
                    Log.v(TAG, "noteID_queried: " + noteID_queried[0]);
                    deleteNote(noteID_queried[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientNotes.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        patientNotes.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<PatientNotesActivity.NotesInfo> notesList) {
        patientNotes.addAll(notesList);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDate, tvVisibility, tvDetails;
        Button editButton, deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.NoteTitleLabel);
            tvDate = itemView.findViewById(R.id.NoteDateLabel);
            tvVisibility = itemView.findViewById(R.id.NoteVisibilityLabel);
            tvDetails = itemView.findViewById(R.id.NoteContentLabel);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }

    public void deleteNote(String note_id){
        FirestoreAPI.getInstance().deleteNote(PatientNotesActivity.userID, note_id, new FirestoreListener<Task>() {

            @Override
            public void getResult(Task object) {
                // do nothing, just generate data
            }

            @Override
            public void getError(Exception e, String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG);
            }
        });
        //to navigate to the dashboard when a note is deleted, in order to update the recycler view
        Intent i = new Intent(context, PatientDashboardActivity.class);
        context.startActivity(i);
    }

}
