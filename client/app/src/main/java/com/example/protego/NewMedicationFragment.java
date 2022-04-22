package com.example.protego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class NewMedicationFragment extends DialogFragment {
    public static String med_name;
    public static String med_dosage;
    public static String med_prescriber;


    //this interface is helpful to connect an event from the dialog to the host activity
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
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_new_medication, null);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //to read the fields for note name and content
                        EditText name_view = (EditText) view.findViewById(R.id.medName);
                        EditText dosage_view = (EditText) view.findViewById(R.id.medDosage);
                        EditText prescriber_view = (EditText) view.findViewById(R.id.medPrescriber);


                        if(name_view.getText().toString().isEmpty() || dosage_view.getText().toString().isEmpty() || prescriber_view.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            med_name = name_view.getText().toString();
                            med_dosage = dosage_view.getText().toString();
                            med_prescriber = prescriber_view.getText().toString();
                        }

                        listener.onDialogPositiveClick(NewMedicationFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewMedicationFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
