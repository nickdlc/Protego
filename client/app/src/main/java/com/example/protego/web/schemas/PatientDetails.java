package com.example.protego.web.schemas;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientDetails {
    public static final String TAG = "PatientDetails";

    public String firstName;
    public String lastName;
    public String lastUpdated;
    public String healthInsuranceNumber;
    public String sex;
    public String bloodType;
    public Integer heightIN;
    public Integer weight;
    public Integer heartRate;
    public String bloodPressure;

    public PatientDetails(){

    }

    public PatientDetails(JSONObject jsonObject) throws JSONException {
        firstName = jsonObject.getString("firstName");
    }

    public static List<PatientDetails> constructPatients (JSONArray patientsArray) throws JSONException {
        List<PatientDetails> patients = new ArrayList<>();
        for(int i = 0; i < patientsArray.length(); i++){
            patients.add(new PatientDetails(patientsArray.getJSONObject(i)));
            Log.d(TAG, "info first name : " + patients.get(i).firstName);
        }
        return patients;
    }
}
