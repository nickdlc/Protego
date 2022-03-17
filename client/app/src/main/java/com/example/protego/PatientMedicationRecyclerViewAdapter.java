package com.example.protego;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientMedicationRecyclerViewAdapter extends RecyclerView.Adapter<PatientMedicationRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PatientMedicationActivity.MedicationInfo> patientMedication;


    public PatientMedicationRecyclerViewAdapter(Context context, ArrayList<PatientMedicationActivity.MedicationInfo> patientMedication) {
        this.context = context;
        this.patientMedication = patientMedication;
    }

    @NonNull
    @Override
    public PatientMedicationRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_medications_medication_container,parent,false);

        return new PatientMedicationRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientMedicationRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(patientMedication.get(position).getName());
        holder.tvDate.setText(patientMedication.get(position).getDate());
        holder.tvDosage.setText(patientMedication.get(position).getDosage());
        holder.tvPrescribedBy.setText(patientMedication.get(position).getPrescribedBy());
    }

    @Override
    public int getItemCount() {
        return patientMedication.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDate, tvDosage, tvPrescribedBy;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.patientMedicationNameText);
            tvDate = itemView.findViewById(R.id.patientMedicationDateText);
            tvDosage = itemView.findViewById(R.id.patientMedicationDosageText);
            tvPrescribedBy = itemView.findViewById(R.id.patientMedicationPrescribedByText);
        }
    }
}
