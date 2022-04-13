package com.example.protego;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.widget.Spinner;

public class NewNoteFragment extends DialogFragment {
//public class OnboardingFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    public boolean visibility = true; //true if public note, 0 if a private note
    private Spinner spinner;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //the spinner component
//        spinner = (Spinner) this.getDialog().findViewById(R.id.noteVisibilitySpinner);
//        //ArrayAdapter
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.note_visibility_navbar_options_array, android.R.layout.simple_spinner_item);
//        //specify layout
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //apply adapter to spinner
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);


        builder.setView(inflater.inflate(R.layout.fragment_new_note, null))
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       //read the fields for note name and content

//                        if(visibility == true){ //add the public note
//
//                        }
//                        else if(visibility ==false) { //add a private note
//
//                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewNoteFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//
//        String userType = (String) parent.getItemAtPosition(pos);
//        Resources resource = getResources();
//        String[] userTypeOptions = resource.getStringArray(R.array.note_visibility_navbar_options_array);
//
//        if(userType.equals(userTypeOptions[0])){
//            visibility = true; //By default a note is public if the user does not specify
//        }
//
//        else if(userType.equals(userTypeOptions[1])){ //the user selected public notes
//            visibility = true;
//        }
//        else if(userType.equals(userTypeOptions[2])) { //the user selected private notes
//            visibility = false;
//        }
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }


}