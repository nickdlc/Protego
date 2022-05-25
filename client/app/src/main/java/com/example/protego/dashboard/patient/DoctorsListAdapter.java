package com.example.protego.dashboard.patient;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.R;
import com.example.protego.web.schemas.firestore.Doctor;

import java.util.ArrayList;

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.ViewHolder> {
    public static final String TAG = "DoctorsListAdapter";

    private ArrayList<Doctor> doctors;
    private RecyclerViewClickListener listener;

    public DoctorsListAdapter(ArrayList<Doctor> doctors, RecyclerViewClickListener listener) {
        this.doctors = doctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsListAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        String doctorName = "Dr. " + doctors.get(position).getLastName();
        holder.tvDoctorName.setText(doctorName);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDoctorName;

        public ViewHolder(@NonNull View view) {
            super(view);
            Log.d("DoctorsListAdapter.ViewHolder", "constructor");
            tvDoctorName = view.findViewById(R.id.tvDoctorName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
