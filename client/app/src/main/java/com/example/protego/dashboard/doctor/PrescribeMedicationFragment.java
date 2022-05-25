package com.example.protego.dashboard.doctor;

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
import com.google.firebase.auth.FirebaseAuth;

public class PrescribeMedicationFragment extends DialogFragment {
    public static final String TAG = "PrescribeMedicationFragment";

    public static String med_name;
    public static String med_dosage;
    public static String med_frequency;
    public static String med_prescriber;

    private FirebaseAuth mAuth;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PrescribeMedicationFragment.NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_prescribe_medication, null);

        builder.setView(view)
                .setPositiveButton("Prescribe", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText etMedName = (EditText) view.findViewById(R.id.etMedName);
                        EditText etMedDosage = (EditText) view.findViewById(R.id.etMedDosage);
                        EditText etMedFrequency = (EditText) view.findViewById(R.id.etMedFrequency);

                        if (etMedName.toString().isEmpty() || etMedDosage.toString().isEmpty() || etMedFrequency.toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            med_name = etMedName.getText().toString();
                            med_dosage = etMedDosage.getText().toString();
                            med_frequency = etMedFrequency.getText().toString();
                        }

                        listener.onDialogPositiveClick(PrescribeMedicationFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PrescribeMedicationFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
