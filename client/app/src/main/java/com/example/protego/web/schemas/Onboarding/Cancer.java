package com.example.protego.web.schemas.Onboarding;

public class Cancer {

    private String cancerID;
    private String cancerType;
    private String dateDiagnosed;
    private String doctorName;


    public String getCancerID() {
        return this.cancerID;
    }
    public void setCancerID(String cancerID) {
        this.cancerID = cancerID;
    }

    public String getTypeOfCancer() {
        return this.cancerType;
    }
    public void setTypeOfCancer(String cancerType) {
        this.cancerType = cancerType;
    }

    public String getDateDiagnosed() {
        return this.dateDiagnosed;
    }

    public void setDateDiagnosed(String month, String day, String year) {
        this.dateDiagnosed = month + "/" + day + "/" + year;
    }

    public String getDoctorName() {
        return this.doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    @Override
    public String toString() {
        return "Cancer{" +
                "cancerID='" + cancerID + '\'' +
                ", cancerType=" + cancerType +
                ", dateDiagnosed=" + dateDiagnosed +
                ", doctorName=" + doctorName +
                '\'' +
                '}';
    }

}
