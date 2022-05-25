package com.example.protego.web.schemas.firestore;

import java.util.Date;

public class MedicalInfo {
    private String infoID;
    private String patient;
    private Date lastUpdated;
    private String healthInsuranceNumber;
    private String sex;
    private String bloodType;
    private Integer heightIN;
    private Integer weight;
    private Integer heartRate;
    private String bloodPressure;

    public String getInfoID() {
        return infoID;
    }

    public void setInfoID(String infoID) {
        this.infoID = infoID;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getHealthInsuranceNumber() {
        return healthInsuranceNumber;
    }

    public void setHealthInsuranceNumber(String healthInsuranceNumber) {
        this.healthInsuranceNumber = healthInsuranceNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Integer getHeightIN() {
        return heightIN;
    }

    public void setHeightIN(Integer heightIN) {
        this.heightIN = heightIN;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    @Override
    public String toString() {
        return "MedicalInfo{" +
                "infoID='" + infoID + '\'' +
                ", patient=" + patient +
                ", lastUpdated=" + lastUpdated +
                ", healthInsuranceNumber='" + healthInsuranceNumber + '\'' +
                ", sex=" + sex +
                ", bloodType='" + bloodType + '\'' +
                ", heightIN=" + heightIN +
                ", weight=" + weight +
                ", heartRate=" + heartRate +
                ", bloodPressure='" + bloodPressure + '\'' +
                '}';
    }
}
