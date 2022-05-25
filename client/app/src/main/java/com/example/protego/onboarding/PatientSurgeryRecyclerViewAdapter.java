package com.example.protego.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.R;
import com.example.protego.web.schemas.onboarding.SurgeryInfo;

import java.util.ArrayList;


public class PatientSurgeryRecyclerViewAdapter extends RecyclerView.Adapter<PatientSurgeryRecyclerViewAdapter.MyViewHolder>{


    Context context;
    ArrayList<SurgeryInfo> SurgeryData;

    public PatientSurgeryRecyclerViewAdapter(Context context, ArrayList<SurgeryInfo> SurgeryData){
        this.context = context;
        this.SurgeryData = SurgeryData;
    }

    @NonNull
    @Override
    public PatientSurgeryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_allergy_row,parent,false);
        return new PatientSurgeryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientSurgeryRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(SurgeryData.get(position).getName());
        holder.tvDiagnosisDate.setText(SurgeryData.get(position).getDate());
        holder.tvDoctorDiagnosed.setText(SurgeryData.get(position).getDoctor());

    }

    @Override
    public int getItemCount() {
        return SurgeryData.size();
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
