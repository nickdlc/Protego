package com.example.Protego.web;

public class AssignedTo {
    private String uid;
    private String patient;
    private String doctor;
    private Boolean active;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
                "uid='" + uid + '\'' +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", active=" + active +
                '}';
    }
}
