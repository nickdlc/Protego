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

        import com.example.protego.web.ServerAPI;
        import com.example.protego.web.ServerRequest;
        import com.example.protego.web.ServerRequestListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.text.BreakIterator;

public class PatientNavigationBarFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "PatientActivity";

    public String saveName = "Menu";
    private String[] navbar_options_array = {saveName, "Home", "Profile", "Log out"};

    private Spinner spinner;

    private FirebaseAuth mAuth;
    private JSONObject patientInfo;
    private String firstName;

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

        ServerAPI.getPatient(currentUser.getUid(), new ServerRequestListener() {
            @Override
            public void recieveCompletedRequest(ServerRequest req) {
                if (req != null && !req.getResultString().equals("")) {
                    Log.d(TAG, "req recieved for patient : " + req.getResult().toString());

                    try {
                        JSONObject pateintJSON = req.getResultJSON();
                        PatientDashboardActivity.Name = pateintJSON.getString("firstName");

                        //to update the first name of the patient on their navbar
                        updateNavbarName(view);

                        //Name = patientDetails.firstName;
                        Log.d(TAG, "info first name : " + PatientDashboardActivity.Name);
                    } catch (JSONException e) {
                        Log.e(TAG, "could not recieve patient info : ", e);
                    }

                } else {
                    Log.d(TAG, "Can't get patient info.");
                }
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
        navbar_options_array[0] = userName+ "'s Settings";
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String userNavbarSelection = (String) parent.getItemAtPosition(pos);
        Resources resource = getResources();

        if (userNavbarSelection.equals(navbar_options_array[0])) { //the user selects their "name + settings" option
            createIntent(PatientDashboardActivity.class);
        }
        else if (userNavbarSelection.equals(navbar_options_array[1])) { //the user selects the empty option which will take them to their dashboard
            createIntent(PatientDashboardActivity.class);
        }

        else if (userNavbarSelection.equals(navbar_options_array[2])) { //the user selects the profile option which will take them to their profile
            //TODO: add the patient profile activity
            //createIntent(PatientProfileActivity.class);
        }

        else if (userNavbarSelection.equals(navbar_options_array[3])) { //the user selects the Log out option which will take them to sign in
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

