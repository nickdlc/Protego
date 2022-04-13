package com.example.protego;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protego.web.FirestoreAPI;
import com.example.protego.web.FirestoreListener;
import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorNavigationBarFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "DoctorDashboardNavbar";
    private Spinner spinner;
    private static String[] navbar_options_array = {"Menu", "Home", "Profile", "Log out"};

    private FirebaseAuth mAuth;


    public DoctorNavigationBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_doctor_navigation_bar, container, false);


        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirestoreAPI.getInstance().getDoctor(currentUser.getUid(), new FirestoreListener<Doctor>() {
            @Override
            public void getResult(Doctor doctor) {
                Log.d(TAG, "req received for doctor : " + doctor);

                if (doctor != null) {
                    DoctorDashboardActivity.lastName = doctor.getLastName();

                    //to update the last name of the doctor on their navbar
                    updateNavbarName(view);

                    Log.d(TAG, "info last name : " + doctor.getLastName());
                } else {
                    Log.e(TAG, "could not receive doctor info : ");
                }
            }

            @Override
            public void getError(Exception e, String msg) {
                Log.e(TAG, "failed to get doctor : " + msg, e);
            }
        });

        //the spinner component
        spinner = view.findViewById(R.id.doctorNavbarSpinner);
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
        String lastName = DoctorDashboardActivity.getName();
        TextView nameTextView = (TextView) view.findViewById(R.id.doctorNameTextView);
        nameTextView.setText(lastName);
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
            try {
                mAuth.signOut();
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
