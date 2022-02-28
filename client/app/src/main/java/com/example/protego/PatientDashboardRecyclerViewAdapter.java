package com.example.protego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientDashboardRecyclerViewAdapter extends RecyclerView.Adapter<PatientDashboardRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<PatientDashboardActivity.PatientInfo> patientData;

    public PatientDashboardRecyclerViewAdapter(Context context, ArrayList<PatientDashboardActivity.PatientInfo> patientData){
        this.context = context;
        this.patientData = patientData;
    }
    @NonNull
    @Override
    public PatientDashboardRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_dashboard_data_row,parent,false);
        return new PatientDashboardRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientDashboardRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(patientData.get(position).getTitle());
        holder.tvDetails.setText(patientData.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return patientData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDetails;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.patientDataTitle);
            tvDetails = itemView.findViewById(R.id.patientDataInfo);
        }
    }
}
