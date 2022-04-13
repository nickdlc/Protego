package com.example.protego;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.PatientDetails;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientMedicationRecyclerViewAdapter extends RecyclerView.Adapter<PatientMedicationRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Medication> medications;


    public PatientMedicationRecyclerViewAdapter(Context context, List<Medication> medications) {
        Log.d("PatientMedicationAdapter", "patientMedicationAdapter");
        this.context = context;
        this.medications = medications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("PatientMedicationAdapter", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_medications_medication_container,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientMedicationRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d("PatientMedicationAdapter", "onBindViewHolder " + position);

        // get the movie at the position
        Medication medication = medications.get(position);
        // bind the movie data into the VH
        holder.bind(medication);
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDate, tvDosage, tvPrescribedBy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.patientMedicationNameText);
            tvDate = itemView.findViewById(R.id.patientMedicationDateText);
            tvDosage = itemView.findViewById(R.id.patientMedicationDosageText);
            tvPrescribedBy = itemView.findViewById(R.id.patientMedicationPrescribedByText);
        }
        public void bind(final Medication medication) {
            tvName.setText(medication.getName());
            tvDate.setText(medication.getDatePrescribed().toString());
            tvDosage.setText(medication.getDosage());
            tvPrescribedBy.setText(medication.getPrescriber());
        }

    }
}
