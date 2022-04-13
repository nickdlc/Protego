package com.example.protego.web.schemas;

import java.util.Date;

public class AssignedTo {

    private Boolean active;
    private String doctor;
    private String patient;

    public AssignedTo() {
    }

    public AssignedTo(Boolean active, String doctor, String patient) {
        this.active = active;
        this.patient = patient;
        this.doctor = doctor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AssignedTo{" +
                "active='" + active + '\'' +
                ", doctor='" + doctor + '\'' +
                ", patient=" + patient + '}';
    }
}
