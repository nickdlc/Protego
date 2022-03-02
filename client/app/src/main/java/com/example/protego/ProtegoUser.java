package com.example.protego;

public class ProtegoUser {
    private String firstName;
    private String lastName;
    private String type;

    public enum ProtegoUserType {
        PATIENT, DOCTOR;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(ProtegoUserType type) {
        this.type = type.name();
    }

    @Override
    public String toString() {
        return "ProtegoUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}