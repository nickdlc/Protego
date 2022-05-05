package com.example.protego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class DoctorNewPatientVitalsFragment extends DialogFragment {
    public static String vitalBloodPressure;
    public static String vitalHeartRate;
    public static String vitalRespiratoryRate;
    public static String vitalTemperature;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    DoctorNewPatientVitalsFragment.NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DoctorNewPatientVitalsFragment.NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_doctor_new_patient_vital, null);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText etHeartRate = view.findViewById(R.id.doctorNewPatientVitalHeartRate);
                        EditText etRespiratoryRate = view.findViewById(R.id.doctorNewPatientVitalRespiratoryRate);
                        EditText etTemperature = view.findViewById(R.id.doctorNewPatientVitalRespiratoryRate);
                        EditText etBloodPressure = view.findViewById(R.id.doctorNewPatientVitalBloodPressure);

                        String heartRateText = etHeartRate.getText().toString();
                        String respiratoryRateText = etRespiratoryRate.getText().toString();
                        String temperatureText = etTemperature.getText().toString();
                        String bloodPressureText = etBloodPressure.getText().toString();

                        if (heartRateText.isEmpty() || respiratoryRateText.isEmpty() || temperatureText.isEmpty() || bloodPressureText.isEmpty()) {
                            Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_LONG).show();
                        } else {
                            vitalBloodPressure = bloodPressureText;
                            vitalHeartRate = heartRateText;
                            vitalRespiratoryRate = respiratoryRateText;
                            vitalTemperature = temperatureText;
                        }
                        listener.onDialogPositiveClick(DoctorNewPatientVitalsFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DoctorNewPatientVitalsFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
