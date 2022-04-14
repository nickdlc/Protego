package com.example.protego;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.protego.web.schemas.Note;
import com.example.protego.web.schemas.Vital;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientVitalsRecyclerViewAdapter extends RecyclerView.Adapter<PatientVitalsRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Vital> patientVitals;

    public PatientVitalsRecyclerViewAdapter(Context context, List<Vital> patientVitals){
        this.context = context;
        this.patientVitals = patientVitals;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_vitals_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("PatientVitalsAdapter", "onBindViewHolder " + position);

        // get the movie at the position
        Vital vital = patientVitals.get(position);
        // bind the movie data into the VH
        holder.bind(vital);
    }

    @Override
    public int getItemCount() {
        return patientVitals.size();
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
        public void bind(final Vital vital) {
            tvDate.setText(vital.getDate().toString());
            tvSource.setText(vital.getSource());
            tvHeartRate.setText(Integer.toString(vital.getHeartRate()));
            tvBloodPressure.setText(vital.getBloodPressure());
            tvRespiratoryRate.setText(Integer.toString(vital.getRespiratoryRate()));
            tvTemperature.setText(Double.toString(vital.getTemperature()));
        }
    }
}
