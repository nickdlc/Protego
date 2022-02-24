package com.example.protego;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class DoctorNavigationBarFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;

    public DoctorNavigationBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_doctor_navigation_bar, container, false);

        //the spinner component
        spinner = view.findViewById(R.id.doctorNavbarSpinner);
        //ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.doctor_navbar_options_array, android.R.layout.simple_spinner_item);
        //specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userNavbarSelection = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();
        String[] userNavbarOptions = resource.getStringArray(R.array.doctor_navbar_options_array);

        if (userNavbarSelection.equals(userNavbarOptions[1])) { //the user selects the empty option which will take them to their dashboard
            createIntent(DoctorDashboardActivity.class);
        }

        else if (userNavbarSelection.equals(userNavbarOptions[2])) { //the user selects the profile option which will take them to their profile
            createIntent(DoctorProfileActivity.class);
        }

        else if (userNavbarSelection.equals(userNavbarOptions[3])) { //the user selects the Log out option which will take them to sign in
            createIntent(SignupActivity.class);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // create an intent to navigate to next activity
    private void createIntent(Class nextActivityClass) {
        Intent i = new Intent(getContext(), nextActivityClass);
        startActivity(i);
    }
}
