package com.example.Protego.web;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class Vital {
    private String vitalID;
    private String patientID;
    private String source;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private Date date;
    private int heartRate;
    private int respiratoryRate;
    private double temperature;
    private String bloodPressure;


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
//    public void setDate(Date date) {
//        this.date = firebase.database.ServerValue.TIMESTAMP;
//    }

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
}
