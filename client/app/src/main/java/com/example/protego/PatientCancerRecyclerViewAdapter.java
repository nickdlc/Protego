package com.example.protego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PatientCancerRecyclerViewAdapter extends RecyclerView.Adapter<PatientCancerRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<NewCancerFragment.CancerInfo> CancerData;

    public PatientCancerRecyclerViewAdapter(Context context, ArrayList<NewCancerFragment.CancerInfo> CancerData) {
        this.context = context;
        this.CancerData = CancerData;
    }

    @NonNull
    @Override
    public PatientCancerRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_allergy_row, parent, false);
        return new PatientCancerRecyclerViewAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PatientCancerRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(CancerData.get(position).getName());
        holder.tvDiagnosisDate.setText(CancerData.get(position).getDate());
        holder.tvDoctorDiagnosed.setText(CancerData.get(position).getDoctor());

    }

    @Override
    public int getItemCount() {
        return CancerData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDiagnosisDate, tvDoctorDiagnosed;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDiagnosisDate = itemView.findViewById(R.id.tvDiagnosisDate);
            tvDoctorDiagnosed = itemView.findViewById(R.id.tvDoctorDiagnosed);

        }
    }
}
