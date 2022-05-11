package com.example.protego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientAllergiesRecyclerViewAdapter extends RecyclerView.Adapter<PatientAllergiesRecyclerViewAdapter.MyViewHolder>{


    Context context;
    ArrayList<NewAllergyFragment.AllergyInfo> AllergyData;

    public PatientAllergiesRecyclerViewAdapter(Context context, ArrayList<NewAllergyFragment.AllergyInfo> AllergyData){
        this.context = context;
        this.AllergyData = AllergyData;
    }

    @NonNull
    @Override
    public PatientAllergiesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_allergy_row,parent,false);
        return new PatientAllergiesRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAllergiesRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(AllergyData.get(position).getName());
        holder.tvDiagnosisDate.setText(AllergyData.get(position).getDate());
        holder.tvDoctorDiagnosed.setText(AllergyData.get(position).getDoctor());

    }

    @Override
    public int getItemCount() {
        return AllergyData.size();
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
