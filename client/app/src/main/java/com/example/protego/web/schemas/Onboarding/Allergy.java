package com.example.protego.web.schemas.Onboarding;

import java.util.Date;
import java.util.List;

public class Allergy {

    private String allergyID;
    private String allergyName;
    private String dateDiagnosed;
    private String doctorDiagnosed;


    public String getAllergyID() {
        return this.allergyID;
    }
    public void setAllergyID(String allergyID) {
        this.allergyID = allergyID;
    }

    public String getAllergyName() {
        return this.allergyName;
    }
    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }

    public String getDateDiagnosed() {
        return this.dateDiagnosed;
    }

    public void setDateDiagnosed(String date) {
        this.dateDiagnosed = date;
    }

    public String getDoctorDiagnosed() {
        return this.doctorDiagnosed;
    }

    public void setDoctorDiagnosed(String doctor) {
        this.doctorDiagnosed = doctorDiagnosed;
    }


    @Override
    public String toString() {
        return "Allergy{" +
                "allergyID='" + allergyID + '\'' +
                ", allergyName=" + allergyName +
                ", dateDiagnosed=" + dateDiagnosed +
                '\'' +
                '}';
    }




}
