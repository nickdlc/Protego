package com.example.protego.web.schemas;

public class Patient extends ProtegoUser {
    private String patientID;
    private String homeAddress;

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID='" + patientID + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                '}';
    }
}
