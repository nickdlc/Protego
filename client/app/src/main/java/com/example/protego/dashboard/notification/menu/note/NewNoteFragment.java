package com.example.protego.dashboard.notification.menu.note;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.protego.R;

public class NewNoteFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    public static boolean visibility = true; //true if public note, 0 if a private note
    public static boolean isVisibilitySelected = false; //true if Note Visibility was selected on the dialog
    private Spinner spinner;
    private static String[] visibility_array = {"Select Note Visibility", "Public", "Private"};
    public static String note_title;
    public static String note_content;


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

        View view = inflater.inflate(R.layout.fragment_new_note, null);

        //the spinner component
        spinner = view.findViewById(R.id.noteVisibilitySpinner);
        //ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, visibility_array);
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
                        EditText title_view = (EditText) view.findViewById(R.id.noteTitle);
                        EditText content_view = (EditText) view.findViewById(R.id.noteContent);

                        if(title_view.getText().toString().isEmpty() && content_view.getText().toString().isEmpty() && isVisibilitySelected == false){ //title and content are empty
                            Toast.makeText(getActivity(), "Please complete the Title, Visibility, Content fields", Toast.LENGTH_SHORT).show();
                        }
                        else if (title_view.getText().toString().isEmpty() && content_view.getText().toString().isEmpty()){ //title and content is empty
                            Toast.makeText(getActivity(), "Please complete the Title and Content fields", Toast.LENGTH_SHORT).show();
                        }

                        else if (title_view.getText().toString().isEmpty() && isVisibilitySelected == false){ //title and visibility is empty
                            Toast.makeText(getActivity(), "Please complete the Title and Visibility fields", Toast.LENGTH_SHORT).show();
                        }

                        else if (content_view.getText().toString().isEmpty() && isVisibilitySelected == false){ //content and visibility is empty
                            Toast.makeText(getActivity(), "Please complete the Content and Visibility fields", Toast.LENGTH_SHORT).show();
                        }

                        else if (title_view.getText().toString().isEmpty()){ //title is empty
                            Toast.makeText(getActivity(), "Please complete the Title field", Toast.LENGTH_SHORT).show();
                        }

                        else if (isVisibilitySelected == false){ //visibility is empty
                            Toast.makeText(getActivity(), "Please complete the Visibility field", Toast.LENGTH_SHORT).show();
                        }

                        else if(content_view.getText().toString().isEmpty()){ //content is empty
                            Toast.makeText(getActivity(), "Please complete the Content field", Toast.LENGTH_SHORT).show();
                        }

                        else{ //content, title, and visibility fields are all completed
                            note_title = title_view.getText().toString();
                            note_content = content_view.getText().toString();
                        }

                        listener.onDialogPositiveClick(NewNoteFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewNoteFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userType = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();
        String[] userTypeOptions = resource.getStringArray(R.array.note_visibility_navbar_options_array);

        if(userType.equals(userTypeOptions[0])){
            isVisibilitySelected = false; //is false when the user does not specify visibility
        }
        else if(userType.equals(userTypeOptions[1])){ //the user selected public notes
            visibility = true;
            isVisibilitySelected = true;
        }
        else if(userType.equals(userTypeOptions[2])) { //the user selected private notes
            visibility = false;
            isVisibilitySelected = true;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


}