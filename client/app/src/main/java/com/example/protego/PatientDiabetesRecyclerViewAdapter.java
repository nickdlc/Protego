package com.example.protego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PatientDiabetesRecyclerViewAdapter extends RecyclerView.Adapter<PatientDiabetesRecyclerViewAdapter.MyViewHolder>{


    Context context;
    ArrayList<NewDiabetesFragment.DiabetesInfo> DiabetesData;

    public PatientDiabetesRecyclerViewAdapter(Context context, ArrayList<NewDiabetesFragment.DiabetesInfo> DiabetesData){
        this.context = context;
        this.DiabetesData = DiabetesData;
    }

    @NonNull
    @Override
    public PatientDiabetesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_allergy_row,parent,false);
        return new PatientDiabetesRecyclerViewAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PatientDiabetesRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(DiabetesData.get(position).getName());
        holder.tvDiagnosisDate.setText(DiabetesData.get(position).getDate());
        holder.tvDoctorDiagnosed.setText(DiabetesData.get(position).getDoctor());

    }

    @Override
    public int getItemCount() {
        return DiabetesData.size();
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
