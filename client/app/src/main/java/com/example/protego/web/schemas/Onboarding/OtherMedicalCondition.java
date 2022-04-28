package com.example.protego.web.schemas.Onboarding;

public class OtherMedicalCondition {


    private String conditionID;
    private String conditionName;
    private String dateDiagnosed;
    private String doctorName;


    public String getConditionID() {
        return this.conditionID;
    }
    public void setConditionID(String conditionID) {
        this.conditionID = conditionID;
    }

    public String getConditionName() {
        return this.conditionName;
    }
    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getDateDiagnosed() {
        return this.dateDiagnosed;
    }

    public void setDateDiagnosed(String date) {
        this.dateDiagnosed = date;
    }

    public String getDoctorName() {
        return this.doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    @Override
    public String toString() {
        return "OtherMedicalCondition{" +
                "conditionID='" + conditionID + '\'' +
                ", conditionName=" + conditionName +
                ", dateDiagnosed=" + dateDiagnosed +
                ", doctorName=" + doctorName +
                '\'' +
                '}';
    }



}
