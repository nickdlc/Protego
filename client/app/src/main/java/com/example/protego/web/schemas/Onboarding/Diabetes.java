package com.example.protego.web.schemas.Onboarding;

public class Diabetes {

    private String diabetesID;
    private String diabetesType;
    private String dateDiagnosed;
    private String doctorName;


    public String getDiabetesID() {
        return this.diabetesID;
    }
    public void setDiabetesID(String diabetesID) {
        this.diabetesID = diabetesID;
    }

    public String getDiabetesType() {
        return this.diabetesType;
    }
    public void setDiabetesType(String diabetesType) {
        this.diabetesType = diabetesType;
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
        return "Diabetes{" +
                "diabetesID='" + diabetesID + '\'' +
                ", diabetesType=" + diabetesType +
                ", dateDiagnosed=" + dateDiagnosed +
                ", doctorName=" + doctorName +
                '\'' +
                '}';
    }
}
