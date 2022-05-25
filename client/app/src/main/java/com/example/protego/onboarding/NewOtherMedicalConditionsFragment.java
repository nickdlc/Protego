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
import com.example.protego.web.schemas.onboarding.OtherConditionsInfo;

import java.util.ArrayList;

public class NewOtherMedicalConditionsFragment extends DialogFragment{

    NoticeDialogListener listener;
    public static String Name;
    public static String Date;
    public static String Doctor;

    public static ArrayList<OtherConditionsInfo> OtherConditionsData = new ArrayList<>();

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

            View view = inflater.inflate(R.layout.fragment_new_other_medical_conditions, null);


            builder.setView(view)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText name_edit = (EditText) view.findViewById(R.id.otherConditionEditText);
                            EditText date_edit = (EditText) view.findViewById(R.id.otherConditionDiagnosisDateEditText);
                            EditText doctor_edit = (EditText) view.findViewById(R.id.otherConditionDoctorEditText);

                            String name = name_edit.getText().toString();
                            String date = date_edit.getText().toString();
                            String doctor = doctor_edit.getText().toString();

                            if (name.isEmpty() || date.isEmpty() || doctor.isEmpty()) { //title and content are empty
                                Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                                listener.onDialogPositiveClick(NewOtherMedicalConditionsFragment.this);

                            }
                            else if(!checkDate(date)){
                                Toast.makeText(getActivity(), "Please complete the date in the correct format", Toast.LENGTH_SHORT).show();
                                listener.onDialogPositiveClick(NewOtherMedicalConditionsFragment.this);

                            }
                            else{
                                Toast.makeText(getActivity(), "Other Medical Condition Information added", Toast.LENGTH_SHORT).show();
                                Name = name;
                                Date = date;
                                Doctor = doctor;
                                OtherConditionsData.add(new OtherConditionsInfo(name, date, doctor));
                                PatientOnboardingActivity.otherConditionView.setVisibility(View.VISIBLE);
                                PatientOnboardingActivity.other_conditions_adapter.notifyDataSetChanged();
                            }

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            NewOtherMedicalConditionsFragment.this.getDialog().cancel();
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