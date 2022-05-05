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

public class PatientMedicationRecyclerViewAdapter extends RecyclerView.Adapter<PatientMedicationRecyclerViewAdapter.MyViewHolder> {
    Context context;
    List<PatientMedicationActivity.MedicationInfo> patientMedication;


    public PatientMedicationRecyclerViewAdapter(Context context, List<PatientMedicationActivity.MedicationInfo> medications) {
        Log.d("PatientMedicationAdapter", "patientMedicationAdapter");
        this.context = context;
        this.patientMedication = medications;
    }

    @NonNull
    @Override
    public PatientMedicationRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("PatientMedicationAdapter", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_medications_medication_container,parent,false);

        return new PatientMedicationRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientMedicationRecyclerViewAdapter.MyViewHolder holder, int position) {
        Log.d("PatientMedicationAdapter", "onBindViewHolder " + position);
        holder.tvName.setText(patientMedication.get(position).getName());
        holder.tvDate.setText(patientMedication.get(position).getDate());
        holder.tvDosage.setText(patientMedication.get(position).getDosage());
        holder.tvFrequency.setText(patientMedication.get(position).getFrequency());
        holder.tvPrescribedBy.setText(patientMedication.get(position).getPrescribedBy());
    }

    @Override
    public int getItemCount() {
        return patientMedication.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        patientMedication.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<PatientMedicationActivity.MedicationInfo> medicationList) {
        patientMedication.addAll(medicationList);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDate, tvDosage, tvPrescribedBy, tvFrequency;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.patientMedicationNameText);
            tvDate = itemView.findViewById(R.id.patientMedicationDateText);
            tvDosage = itemView.findViewById(R.id.patientMedicationDosageText);
            tvPrescribedBy = itemView.findViewById(R.id.patientMedicationPrescribedByText);
            tvFrequency = itemView.findViewById(R.id.patientMedicationFrequencyText);
        }
    }
}
