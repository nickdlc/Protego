package com.example.protego.web.schemas;

public class Patient extends ProtegoUser {
    private String patientID;

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientID() {
        return this.patientID;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID='" + patientID + '\'' +
                '}';
    }
}
