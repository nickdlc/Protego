package com.example.protego.web.schemas.Onboarding;


public class AllergyInfo {
    private final String name;
    private final String date;
    private final String doctor;

    public AllergyInfo(String name, String date, String doctor) {
        this.name = name;
        this.date = date;
        this.doctor = doctor;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getDoctor() {
        return doctor;
    }


}