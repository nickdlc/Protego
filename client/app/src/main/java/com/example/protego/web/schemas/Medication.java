package com.example.protego.web.schemas;

import java.util.Date;

public class Medication {
    private String medID;
    private String name;
    private String prescribee; // a patient
    private Date datePrescribed;
//    private Map<String, String> dosage;

    private String dosage;
    private String prescriber; // a doctor

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

    @Override
    public String toString() {
        return "Medication{" +
                "medID='" + medID + '\'' +
                ", name=" + name +
                ", prescribee=" + prescribee +
                ", datePrescribed=" + datePrescribed +
                ", dosage='" + dosage + '\'' +
                ", prescriber=" + prescriber +
                '}';
    }
}