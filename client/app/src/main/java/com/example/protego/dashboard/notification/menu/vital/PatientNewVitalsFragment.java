package com.example.protego.dashboard.notification.menu.vital;

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

import com.example.protego.R;

public class PatientNewVitalsFragment extends DialogFragment {
    public static final String TAG = "PatientNewVitalsFragment";

    public static String vitalBloodPressure;
    public static String vitalHeartRate;
    public static String vitalRespiratoryRate;
    public static String vitalSource;
    public static String vitalTemperature;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_patient_new_vital, null);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText etHeartRate = view.findViewById(R.id.patientNewVitalHeartRate);
                        EditText etRespiratoryRate = view.findViewById(R.id.patientNewVitalRespiratoryRate);
                        EditText etTemperature = view.findViewById(R.id.patientNewVitalTemperature);
                        EditText etBloodPressure = view.findViewById(R.id.patientNewVitalBloodPressure);
                        EditText etSourceDoctor = view.findViewById(R.id.patientNewVitalSourceDoctor);

                        String heartRateText = etHeartRate.getText().toString();
                        String respiratoryRateText = etRespiratoryRate.getText().toString();
                        String temperatureText = etTemperature.getText().toString();
                        String bloodPressureText = etBloodPressure.getText().toString();
                        String sourceDoctorText = etSourceDoctor.getText().toString();

                        if (heartRateText.isEmpty() || respiratoryRateText.isEmpty() || temperatureText.isEmpty() || bloodPressureText.isEmpty() || sourceDoctorText.isEmpty()) {
                            Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_LONG).show();
                        } else {
                            vitalBloodPressure = bloodPressureText;
                            vitalHeartRate = heartRateText;
                            vitalRespiratoryRate = respiratoryRateText;
                            vitalSource = sourceDoctorText;
                            vitalTemperature = temperatureText;
                        }
                        listener.onDialogPositiveClick(PatientNewVitalsFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PatientNewVitalsFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
