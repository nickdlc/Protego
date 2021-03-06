package com.example.protego.onboarding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.protego.listeners.NoticeDialogListener;
import com.example.protego.R;
import com.example.protego.web.schemas.onboarding.CancerInfo;

import java.util.ArrayList;


public class NewCancerFragment extends DialogFragment {
        NoticeDialogListener listener;
        public static String Name;
        public static String Date;
        public static String Doctor;



    public static ArrayList<CancerInfo> CancerData = new ArrayList<>();

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

            View view = inflater.inflate(R.layout.fragment_new_cancer, null);

            builder.setView(view)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //to read the fields for note name and content
                        EditText name_edit = (EditText) view.findViewById(R.id.cancerTypeEditText);
                        EditText date_edit = (EditText) view.findViewById(R.id.cancerDiagnosisDateEditText);
                        EditText doctor_edit = (EditText) view.findViewById(R.id.cancerDoctorEditText);

                        String name = name_edit.getText().toString();
                        String date = date_edit.getText().toString();
                        String doctor = doctor_edit.getText().toString();

                        if (name.isEmpty() || date.isEmpty() || doctor.isEmpty()) { //title and content are empty
                            Toast.makeText(getActivity(), "Please complete all fields correctly", Toast.LENGTH_SHORT).show();
                            listener.onDialogPositiveClick(NewCancerFragment.this);

                        }
                        else if(!checkDate(date)){
                            Toast.makeText(getActivity(), "Please complete the date in the correct format", Toast.LENGTH_SHORT).show();
                            listener.onDialogPositiveClick(NewCancerFragment.this);

                        }
                        else{
                            Toast.makeText(getActivity(), "Cancer Information added", Toast.LENGTH_SHORT).show();
                            Name = name;
                            Date = date;
                            Doctor = doctor;
                            CancerData.add(new CancerInfo(name, date, doctor));
                            PatientOnboardingActivity.cancerView.setVisibility(View.VISIBLE);
                            PatientOnboardingActivity.cancer_adapter.notifyDataSetChanged();
                        }
                    }

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NewCancerFragment.this.getDialog().cancel();
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