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
    ArrayList<PatientNotesActivity.NotesInfo> patientNotesData;

    public PatientNotesRecyclerViewAdapter(Context context, ArrayList<PatientNotesActivity.NotesInfo> patientNotesData){
        this.context = context;
        this.patientNotesData = patientNotesData;
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
        holder.tvTitle.setText(patientNotesData.get(position).getTitle());
        holder.tvDate.setText(patientNotesData.get(position).getDate());
        holder.tvVisibility.setText(patientNotesData.get(position).getVisibility());
        holder.tvDetails.setText(patientNotesData.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return patientNotesData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDate,tvVisibility,tvDetails;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.patientNotesTitleText);
            tvDate = itemView.findViewById(R.id.patientNotesDateText);
            tvVisibility = itemView.findViewById(R.id.patientNotesVisibilityText);
            tvDetails = itemView.findViewById(R.id.patientNotesDetailsText);
        }
    }
}
