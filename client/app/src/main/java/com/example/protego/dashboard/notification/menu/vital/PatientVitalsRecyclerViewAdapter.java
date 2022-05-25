package com.example.protego.dashboard.notification.menu.vital;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.R;

import java.util.List;

public class PatientVitalsRecyclerViewAdapter extends RecyclerView.Adapter<PatientVitalsRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<PatientVitals.VitalsInfo> patientVitals;

    public PatientVitalsRecyclerViewAdapter(Context context, List<PatientVitals.VitalsInfo> patientVitals){
        this.context = context;
        this.patientVitals = patientVitals;
    }
    @NonNull
    @Override
    public PatientVitalsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_vitals_row,parent,false);
        return new PatientVitalsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientVitalsRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d("PatientVitalsAdapter", "onBindViewHolder " + position);

        holder.tvDate.setText(patientVitals.get(position).getDate());
        holder.tvSource.setText(patientVitals.get(position).getSource());
        holder.tvHeartRate.setText(Integer.toString(patientVitals.get(position).getHeartRate()));
        holder.tvBloodPressure.setText(patientVitals.get(position).getBloodPressure());
        holder.tvRespiratoryRate.setText(Integer.toString(patientVitals.get(position).getRespiratoryRate()));
        holder.tvTemperature.setText(Double.toString(patientVitals.get(position).getTemperature()));
    }

    @Override
    public int getItemCount() {
        return patientVitals.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        patientVitals.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvSource, tvHeartRate, tvBloodPressure, tvRespiratoryRate, tvTemperature;
        public ViewHolder(@NonNull View itemView) {
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
