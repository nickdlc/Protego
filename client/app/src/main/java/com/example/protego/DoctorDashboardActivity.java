package com.example.protego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.protego.web.ServerAPI;
import com.example.protego.web.ServerRequest;
import com.example.protego.web.ServerRequestListener;
import com.example.protego.web.schemas.DoctorDetails;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorDashboardActivity extends AppCompatActivity{
    public static final String TAG = "DoctorActivity";

    //input fields
    private Button button;
    private DoctorDetails doctorInfo;
    private FirebaseAuth mAuth;

    //to store the doctor's last name
    private static String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        // update the user's last name based on their profile information
        //TODO: update this last name according to the database
        lastName = "Example";

        //Connects the Scan QR Code button to the QR Code activity
        connectButtonToActivity(R.id.DoctorViewPatientsButton, DoctorViewPatientsActivity.class);
        //Connects the View Patients button to the View Patients Activity
        connectButtonToActivity(R.id.DoctorScanQRCodeButton, DoctorScanQRCodeActivity.class);

        doctorInfo = new DoctorDetails();
        mAuth = FirebaseAuth.getInstance();
        // manually write in doctor uid for now
        getDoctorInfo(mAuth.getCurrentUser().getUid());
    }

    // navigate to next activity
    private void connectButtonToActivity(Integer buttonId, Class nextActivityClass) {

        button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), nextActivityClass);
                startActivity(i);
                finish();
            }
        });
    }

    private void getDoctorInfo(String duid) {
        ServerAPI.getDoctor(duid, new ServerRequestListener() {
            @Override
            public void receiveCompletedRequest(ServerRequest req) {
                if (req != null && !req.getResultString().equals("")) {
                    Log.d(TAG, "req recieved for doctor : " + req.getResult().toString());

                    try {
                        JSONObject doctorJSON = req.getResultJSON();

                        doctorInfo.firstName = doctorJSON.getString("firstName");
                        doctorInfo.lastName = doctorJSON.getString("lastName");
                        Log.d(TAG, "info first name : " + doctorJSON.getString("firstName"));
                    } catch (JSONException e) {
                        Log.e(TAG, "could not recieve doctor info : ", e);
                    }
                } else {
                    Log.d(TAG, "failed to get doctor : " + req.toString());
                }
            }

            @Override
            public void receiveError(Exception e, String msg) {
                Toast.makeText(DoctorDashboardActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

    //function to call from the navbar fragment class to update the navbar according to the doctor last name
    public static String getName(){
        return lastName;
    }
}

