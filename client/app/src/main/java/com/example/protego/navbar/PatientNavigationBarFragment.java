package com.example.protego.navbar;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.protego.dashboard.patient.PatientDashboardActivity;
import com.example.protego.profile.PatientProfileActivity;
import com.example.protego.R;
import com.example.protego.auth.LoginActivity;
import com.example.protego.web.firestore.FirestoreAPI;
import com.example.protego.web.firestore.FirestoreListener;
import com.example.protego.web.schemas.firestore.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientNavigationBarFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "PatientDashboardNavbar";
    private static String[] navbar_options_array = {"Menu", "Home", "Profile", "Log out"};

    private Spinner spinner;
    private FirebaseAuth mAuth;


    public PatientNavigationBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient_navigation_bar, container, false);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirestoreAPI.getInstance().getPatient(currentUser.getUid(), new FirestoreListener<Patient>() {
            @Override
            public void getResult(Patient patient) {
                Log.d(TAG, "req received for patient : " + patient);

                if (patient != null) {
                    PatientDashboardActivity.Name = patient.getFirstName();
                    //LoginActivity.userT = patient.getUserType();

                    Log.d(TAG, "info first name : " + patient.getFirstName());
                    Log.d(TAG, "info user type : " + patient.getUserType());
                    //to update the first name of the patient on their navbar
                    updateNavbarName(view);


                    Log.d(TAG, "info first name : " + patient.getFirstName());
                } else {
                    Log.e(TAG, "could not receive patient info : ");
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "failed to get patient : " + msg, e);
            }
        });





        //the spinner component
        spinner = view.findViewById(R.id.patientNavbarSpinner);
        //ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, navbar_options_array);
        //specify layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //Inflate the layout for this fragment
        return view;
    }


    //updates the patientNameTextView according to the patient's first name
    public void updateNavbarName(View view){
        String userName = PatientDashboardActivity.getName();
        TextView nameTextView = (TextView) view.findViewById(R.id.patientNameTextView);
        nameTextView.setText(userName);
        //navbar_options_array[0] = Character.toString(userName.charAt(0)) + "'s Settings";

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userNavbarSelection = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();

        if (userNavbarSelection.equals(navbar_options_array[1])) { //the user selects the empty option which will take them to their dashboard
            createIntent(PatientDashboardActivity.class);
        }

        else if (userNavbarSelection.equals(navbar_options_array[2])) { //the user selects the profile option which will take them to their profile
            createIntent(PatientProfileActivity.class);
        }

        else if (userNavbarSelection.equals(navbar_options_array[3])) { //the user selects the Log out option which will take them to sign in

            try {
                mAuth.signOut();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                createIntent(LoginActivity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

