package com.example.protego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientVitalsRecyclerViewAdapter extends RecyclerView.Adapter<PatientVitalsRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PaintVitals.VitalsInfo> patientData;

    public PatientVitalsRecyclerViewAdapter(Context context, ArrayList<PaintVitals.VitalsInfo> patientData){
        this.context = context;
        this.patientData = patientData;
    }
    @NonNull
    @Override
    public PatientVitalsRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_vitals_row,parent,false);
        return new PatientVitalsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientVitalsRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvDate.setText(patientData.get(position).getDate());
        holder.tvSource.setText(patientData.get(position).getSource());
        holder.tvHeartRate.setText(patientData.get(position).getHeartRate());
        holder.tvBloodPressure.setText(patientData.get(position).getBloodPressure());
        holder.tvRespiratoryRate.setText(patientData.get(position).getRespiratoryRate());
        holder.tvTemperature.setText(patientData.get(position).getTemperature());
    }

    @Override
    public int getItemCount() {
        return patientData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvSource, tvHeartRate, tvBloodPressure, tvRespiratoryRate, tvTemperature;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvVitalsDate);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvHeartRate = itemView.findViewById(R.id.tvHeartRate);
            tvBloodPressure = itemView.findViewById(R.id.tvBloodPressure);
            tvRespiratoryRate = itemView.findViewById(R.id.tvRespiratoryRate);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
        }
    }
}
