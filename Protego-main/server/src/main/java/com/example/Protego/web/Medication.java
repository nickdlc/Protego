package com.example.Protego.web;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

public class Medication {
    private String medID;
    private String prescribee; // a patient
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private Date datePrescribed;
    private Map<String, String> dosage;
    private String prescriber; // a doctor

    public String getMedID() {
        return medID;
    }

    public void setMedID(String medID) {
        this.medID = medID;
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

    public Map<String, String> getDosage() {
        return dosage;
    }

    public void setDosage(Map<String, String> dosage) {
        this.dosage = dosage;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "medID='" + medID + '\'' +
                ", prescribee=" + prescribee +
                ", date=" + datePrescribed +
                ", dosage='" + dosage + '\'' +
                ", prescriber=" + prescriber +
                '}';
    }
}
