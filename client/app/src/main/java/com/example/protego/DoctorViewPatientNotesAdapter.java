package com.example.protego;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorViewPatientNotesAdapter extends RecyclerView.Adapter<DoctorViewPatientNotesAdapter.MyViewHolder> {
    Context context;

    List<PatientNotesActivity.NotesInfo> patientNotes;
    public static final String TAG = "PatientNotesRecyclerViewAdapter";

    public DoctorViewPatientNotesAdapter(Context context, List<PatientNotesActivity.NotesInfo> patientNotes) {
        Log.d("PatientNotesAdapter", "patientNotesAdapter");
        this.context = context;
        this.patientNotes = patientNotes;
    }

    @NonNull
    @Override
    public DoctorViewPatientNotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("PatientNotesAdapter", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doctor_view_patient_notes,parent,false);

        return new DoctorViewPatientNotesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewPatientNotesAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(patientNotes.get(position).getTitle());
        holder.tvDate.setText(patientNotes.get(position).getDate());
        holder.tvVisibility.setText(patientNotes.get(position).getVisibility());
        holder.tvDetails.setText(patientNotes.get(position).getDetails());
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
        Button deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.NoteTitleLabel);
            tvDate = itemView.findViewById(R.id.NoteDateLabel);
            tvVisibility = itemView.findViewById(R.id.NoteVisibilityLabel);
            tvDetails = itemView.findViewById(R.id.NoteContentLabel);

            deleteButton = itemView.findViewById(R.id.deleteButton);

        }
    }

}
