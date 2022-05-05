package com.example.protego.web.schemas;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Medication {
    public static final String TAG = "Medication";

    private String medID;
    private String name;
    private String prescribee; // a patient
    private Date datePrescribed;
    private List<String> approvedDoctors;
    private String dosage;
    private String prescriber; // a doctor
    private String frequency;

    public String getMedID() {
        return medID;
    }

    public void setMedID(String medID) {
        this.medID = medID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrescribee() {
        return prescribee;
    }

    public void setPrescribee(String prescribee) {
        this.prescribee = prescribee;
    }

    public Date getDatePrescribed() {
        return datePrescribed;
    }

    public void setDatePrescribed(Date date) {
        this.datePrescribed = date;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Medication(){

    }
    public Medication(String name, Date date, String dosage, String prescribedBy) {
        this.name = name;
        datePrescribed = date;
        this.dosage = dosage;
        prescriber = prescribedBy;
    }

    public List<String> getApprovedDoctors() {
        return approvedDoctors;
    }

    public void setApprovedDoctors(List<String> approvedDoctors) {
        this.approvedDoctors = approvedDoctors;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "medID='" + medID + '\'' +
                ", name='" + name + '\'' +
                ", prescribee='" + prescribee + '\'' +
                ", datePrescribed=" + datePrescribed +
                ", approvedDoctors=" + approvedDoctors +
                ", dosage='" + dosage + '\'' +
                ", prescriber='" + prescriber + '\'' +
                ", frequency='" + frequency + '\'' +
                '}';
    }
}