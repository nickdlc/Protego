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


public class NewDiabetesFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static String[] Type_Diabetes_Array = {"Select Diabetes Type","Type 1", "Type 2"};

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
                            //to read the fields for note name and content
//                            EditText title_view = (EditText) view.findViewById(R.id.noteTitle);
//                            EditText content_view = (EditText) view.findViewById(R.id.noteContent);
                            listener.onDialogPositiveClick(NewDiabetesFragment.this);
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

            String userType = (String) parent.getItemAtPosition(pos);

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {


        }

}