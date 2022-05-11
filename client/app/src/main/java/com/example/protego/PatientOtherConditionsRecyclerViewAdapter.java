package com.example.protego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientOtherConditionsRecyclerViewAdapter extends RecyclerView.Adapter<PatientOtherConditionsRecyclerViewAdapter.MyViewHolder>{


    Context context;
    ArrayList<NewOtherMedicalConditionsFragment.OtherConditionsInfo> OtherConditionsData;

    public PatientOtherConditionsRecyclerViewAdapter(Context context, ArrayList<NewOtherMedicalConditionsFragment.OtherConditionsInfo> OtherConditionsData){
        this.context = context;
        this.OtherConditionsData = OtherConditionsData;
    }

    @NonNull
    @Override
    public PatientOtherConditionsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_allergy_row,parent,false);
        return new PatientOtherConditionsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientOtherConditionsRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(OtherConditionsData.get(position).getName());
        holder.tvDiagnosisDate.setText(OtherConditionsData.get(position).getDate());
        holder.tvDoctorDiagnosed.setText(OtherConditionsData.get(position).getDoctor());

    }

    @Override
    public int getItemCount() {
        return OtherConditionsData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDiagnosisDate, tvDoctorDiagnosed;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDiagnosisDate = itemView.findViewById(R.id.tvDiagnosisDate);
            tvDoctorDiagnosed = itemView.findViewById(R.id.tvDoctorDiagnosed);

        }
    }
}