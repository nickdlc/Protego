package com.example.protego;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientNotesRecyclerViewAdapter extends RecyclerView.Adapter<PatientNotesRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PatientNotesActivity.NotesInfo> patientNotes;


    public PatientNotesRecyclerViewAdapter(Context context, ArrayList<PatientNotesActivity.NotesInfo> patientNotes) {
        this.context = context;
        this.patientNotes = patientNotes;
    }

    @NonNull
    @Override
    public PatientNotesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_notes_note_container,parent,false);

        return new PatientNotesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientNotesRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(patientNotes.get(position).getTitle());
        holder.tvDate.setText(patientNotes.get(position).getDate());
        holder.tvVisibility.setText(patientNotes.get(position).getVisibility());
        holder.tvDetails.setText(patientNotes.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return patientNotes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDate, tvVisibility, tvDetails;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.patientMedicationDateLabel);
            tvDate = itemView.findViewById(R.id.patientMedicationNameLabel);
            tvVisibility = itemView.findViewById(R.id.patientMedicationDosageLabel);
            tvDetails = itemView.findViewById(R.id.patientMedicationPrescribedByLabel);
        }
    }
}
