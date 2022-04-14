package com.example.protego;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.Note;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientNotesRecyclerViewAdapter extends RecyclerView.Adapter<PatientNotesRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Note> patientNotes;


    public PatientNotesRecyclerViewAdapter(Context context, List<Note> patientNotes) {
        Log.d("PatientNotesAdapter", "patientNotesAdapter");
        this.context = context;
        this.patientNotes = patientNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("PatientNotesAdapter", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_notes_note_container,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("PatientNotesAdapter", "onBindViewHolder " + position);

        // get the movie at the position
        Note note = patientNotes.get(position);
        // bind the movie data into the VH
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return patientNotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDate, tvVisibility, tvDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.patientMedicationDateLabel);
            tvDate = itemView.findViewById(R.id.patientMedicationNameLabel);
            tvVisibility = itemView.findViewById(R.id.patientMedicationDosageLabel);
            tvDetails = itemView.findViewById(R.id.patientMedicationPrescribedByLabel);
        }
        public void bind(final Note note) {
            tvTitle.setText(note.getTitle());
            tvDate.setText(note.getDateCreated().toString());
            tvVisibility.setText(note.getVisibility());
            tvDetails.setText(note.getContent());
        }
    }
}
