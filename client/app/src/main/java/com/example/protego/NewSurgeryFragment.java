package com.example.protego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewSurgeryFragment extends DialogFragment {

    NoticeDialogListener listener;
    public static String Name;
    public static String Date;
    public static String Doctor;

    public static ArrayList<NewSurgeryFragment.SurgeryInfo> SurgeryData = new ArrayList<>();

    public static class SurgeryInfo {
        private final String name;
        private final String date;
        private final String doctor;

        public SurgeryInfo(String name, String date, String doctor) {
            this.name = name;
            this.date = date;
            this.doctor = doctor;
        }

        public String getDate() {
            return date;
        }

        public String getName() {
            return name;
        }

        public String getDoctor() {
            return doctor;
        }


    }


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

        View view = inflater.inflate(R.layout.fragment_new_surgery, null);

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //to read the fields for note name and content

                        EditText name_edit = (EditText) view.findViewById(R.id.surgeryNameEditText);
                        EditText date_edit = (EditText) view.findViewById(R.id.surgeryDiagnosisDateEditText);
                        EditText doctor_edit = (EditText) view.findViewById(R.id.surgeryDoctorEditText);

                        String name = name_edit.getText().toString();
                        String date = date_edit.getText().toString();
                        String doctor = doctor_edit.getText().toString();

                        if (name.isEmpty() || date.isEmpty() || doctor.isEmpty()) { //title and content are empty
                            Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                            listener.onDialogPositiveClick(NewSurgeryFragment.this);

                        } else if(!checkDate(date)){
                            Toast.makeText(getActivity(), "Please complete the date in the correct format", Toast.LENGTH_SHORT).show();
                            listener.onDialogPositiveClick(NewSurgeryFragment.this);

                        }
                        else{
                            Toast.makeText(getActivity(), "Surgery added", Toast.LENGTH_SHORT).show();
                            Name = name;
                            Date = date;
                            Doctor = doctor;
                            SurgeryData.add(new SurgeryInfo(name, date, doctor));
                            PatientOnboardingActivity.surgeryView.setVisibility(View.VISIBLE);
                            PatientOnboardingActivity.surgery_adapter.notifyDataSetChanged();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewSurgeryFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }



    public boolean checkDate(String date){
        if(date.toCharArray()[2] != '/' || date.toCharArray()[5] != '/' || date.toCharArray().length != 10){
            return false;
        }
        return true;
    }


}