package com.example.protego.web.schemas.firestore;

import java.util.Date;

public class Vital implements Comparable<Vital> {
    private String vitalID;
    private String patientID;
    private String source;
    private Date date;
    private int heartRate;
    private int respiratoryRate;
    private double temperature;
    private String bloodPressure;

    public Vital(){

    }

    public Vital(int heartRate, int respiratoryRate, double temperature, Date date, String bloodPressure, String source) {
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.temperature = temperature;
        this.date = date;
        this.bloodPressure = bloodPressure;
        this.source = source;
    }

    public String getVitalID() {
        return vitalID;
    }
    public void setVitalID(String vitalID) {
        this.vitalID = vitalID;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getPatientID() {
        return patientID;
    }
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }


    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    @Override
    public String toString() {
        return "VITAL{" +
                "vitalID='" + vitalID + '\'' +
                ", source=" + source +
                ", puid=" + patientID +
                ", date=" + date +
                ", heartRate=" + heartRate +
                ", respiratoryRate=" + respiratoryRate +
                ", temperature=" + temperature +
                ", bloodPressure=" + bloodPressure
                + '}';
    }

    @Override
    public int compareTo(Vital v) {
        return getDate().compareTo(v.getDate());
    }
}