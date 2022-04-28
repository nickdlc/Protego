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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class NewDiabetesFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static String[] Type_Diabetes_Array = {"Select Diabetes Type","Type 1", "Type 2"};
    NoticeDialogListener listener;
    public static String Name;
    public static String Date;
    public static String Doctor;


    public static ArrayList<NewDiabetesFragment.DiabetesInfo> DiabetesData = new ArrayList<>();

    public static class DiabetesInfo {
            private final String name;
            private final String date;
            private final String doctor;

            public DiabetesInfo(String name, String date, String doctor) {
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

            View view = inflater.inflate(R.layout.fragment_new_diabetes, null);

            //the spinner component
            Spinner spinner = (Spinner) view.findViewById(R.id.diabetesTypeSpinner);
            //ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Type_Diabetes_Array);
            //specify layout
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //apply adapter to spinner
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            builder.setView(view)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText date_edit = (EditText) view.findViewById(R.id.diabetesDiagnosisDateEditText);
                            EditText doctor_edit = (EditText) view.findViewById(R.id.diabetesDoctorEditText);


                            String date = date_edit.getText().toString();
                            String doctor = doctor_edit.getText().toString();

                            if (Name.equals(Type_Diabetes_Array[0]) || date.isEmpty() || doctor.isEmpty()) { //title and content are empty
                                Toast.makeText(getActivity(), "Please complete all fields correctly", Toast.LENGTH_SHORT).show();
                                listener.onDialogPositiveClick(NewDiabetesFragment.this);

                            } else if(!checkDate(date)){
                                Toast.makeText(getActivity(), "Please complete the date in the correct format", Toast.LENGTH_SHORT).show();
                                listener.onDialogPositiveClick(NewDiabetesFragment.this);

                            }
                            else{
                                Toast.makeText(getActivity(), "Diabetes Information added", Toast.LENGTH_SHORT).show();
                                Date = date;
                                Doctor = doctor;
                                DiabetesData.add(new DiabetesInfo(Name, Date, Doctor));
                                PatientOnboardingActivity.diabetesView.setVisibility(View.VISIBLE);
                                PatientOnboardingActivity.diabetes_adapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NewDiabetesFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Name = (String) parent.getItemAtPosition(pos);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        public boolean checkDate(String date){
            if(date.toCharArray()[2] != '/' || date.toCharArray()[5] != '/' || date.toCharArray().length != 10){
                return false;
            }
            return true;
        }




}