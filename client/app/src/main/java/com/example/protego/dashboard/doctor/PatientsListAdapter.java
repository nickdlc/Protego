package com.example.protego.dashboard.doctor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.protego.R;
import com.example.protego.web.schemas.details.PatientDetails;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientsListAdapter extends RecyclerView.Adapter<PatientsListAdapter.ViewHolder>{

    Context context;
    List<PatientDetails> patients;

    public PatientsListAdapter(Context context, List<PatientDetails> patients) {
        Log.d("PatientsListAdapter", "patientsListAdapter");
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("PatientsListAdapter", "onCreateViewHolder");
        View patientView = LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(patientView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsListAdapter.ViewHolder holder, int position) {
        Log.d("PatientsListAdapter", "onBindViewHolder " + position);

        // get the movie at the position
        PatientDetails patient = patients.get(position);
        // bind the movie data into the VH
        holder.bind(patient);
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvPatientName;
        //ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            //ivPhoto = itemView.findViewById(R.id.ivPhoto);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final PatientDetails patient) {
            tvPatientName.setText(patient.firstName + " " + patient.lastName);
            System.out.println("testing2" + patient.firstName);

            // register click listener on the whole row

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // navigate to a new activity on tap
                    Intent i = new Intent(context, DoctorViewPatientSelections.class);
                    i.putExtra("patientFirst", patient.firstName);
                    i.putExtra("patientLast", patient.lastName);
                    i.putExtra("patientId", patient.id);
                    i.putExtra("onboardingFlag", patient.onboardingFlag);
                    context.startActivity(i);
                }
            });


        }
    }
}
