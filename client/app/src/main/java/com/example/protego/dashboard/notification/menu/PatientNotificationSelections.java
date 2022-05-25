
package com.example.protego.dashboard.notification.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.protego.R;
import com.example.protego.dashboard.patient.PatientQRCodeDisplay;
import com.example.protego.dashboard.notification.menu.note.PatientNotesActivity;

@Deprecated

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientNotificationSelections#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientNotificationSelections extends Fragment{

    //input parameters
    Button button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientNotificationSelections() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientNotificationSelections.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientNotificationSelections newInstance(String param1, String param2) {
        PatientNotificationSelections fragment = new PatientNotificationSelections();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_notification_selections, container, false);

        //Connect to Medication Activity
        //TODO: update the activity to PatientMedicationActivity once the Medication Activity is made
        connectButtonToActivity(view, R.id.notificationMedicationButton, PatientNotesActivity.class);
        //Connect to Notes Activity
        connectButtonToActivity(view, R.id.notificationNotesButton, PatientNotesActivity.class);
        //Connect to Vitals Activity
        //TODO: update the activity to PatientVitalActivity once the Vital Activity is merged
        connectButtonToActivity(view, R.id.notificationVitalsButton, PatientNotesActivity.class);
        //Connect to View QR Code Activity
        connectButtonToActivity(view, R.id.notificationViewQRCodeButton, PatientQRCodeDisplay.class);

        return view;

    }

    // navigate to next activity within a fragment
    private void connectButtonToActivity(View view, Integer buttonId, Class nextActivityClass) {

        button = view.findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
            }
        });
    }

}