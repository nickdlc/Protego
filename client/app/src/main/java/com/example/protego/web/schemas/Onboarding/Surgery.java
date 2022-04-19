package com.example.protego.web.schemas.Onboarding;

public class Surgery {

    private String surgeryID;
    private String surgeryName;
    private String datePerformed;
    private String doctorName;


    public String getSurgeryID() {
        return this.surgeryID;
    }
    public void setSurgeryID(String surgeryID) {
        this.surgeryID = surgeryID;
    }

    public String getSurgeryName() {
        return this.surgeryName;
    }
    public void setSurgeryName(String surgeryName) {
        this.surgeryName = surgeryName;
    }

    public String getDatePerformed() {
        return this.datePerformed;
    }

    public void setDatePerformed(String month, String day, String year) {
        this.datePerformed = month + "/" + day + "/" + year;
    }

    public String getDoctorName() {
        return this.doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    @Override
    public String toString() {
        return "Surgery{" +
                "surgeryID='" + surgeryID + '\'' +
                ", surgeryName=" + surgeryName +
                ", datePerformed=" + datePerformed +
                ", doctorName=" + doctorName +
                '\'' +
                '}';
    }





}
