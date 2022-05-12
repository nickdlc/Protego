package com.example.protego.web.schemas;

public class Patient extends ProtegoUser {
    private String patientID;
    private String homeAddress;
    private String onboardingFlag;

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

    public String getOnboardingFlag() {
        return onboardingFlag;
    }

    public void setOnboardingFlag(String oboardingFlag) {
        this.onboardingFlag = oboardingFlag;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientID='" + patientID + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                ", Onboarding Completed='" + onboardingFlag + '\'' +
                '}';
    }
}
