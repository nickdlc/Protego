package com.example.protego;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoctorViewPatientsRecyclerView extends RecyclerView.Adapter<DoctorViewPatientsRecyclerView.MyViewHolder> {
    Context context;
    ArrayList<DoctorViewPatientsActivity.PatientInfo> patientData;

    public DoctorViewPatientsRecyclerView(Context context, ArrayList<DoctorViewPatientsActivity.PatientInfo> patientData){
        this.context = context;
        this.patientData = patientData;
    }
    @NonNull
    @Override
    public DoctorViewPatientsRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_patient_row,parent,false);
        return new DoctorViewPatientsRecyclerView.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewPatientsRecyclerView.MyViewHolder holder, int position) {
        holder.tvName.setText(patientData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return patientData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.patientNameText);
        }
    }
}
