package com.example.protego.web.schemas;

public class ProtegoUser implements Comparable<ProtegoUser> {
    private String firstName;
    private String lastName;
    private String email;
    private String userType;

    public enum ProtegoUserType {
        PATIENT, DOCTOR;
    }
    public ProtegoUser() { }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProtegoUserType getUserType() {
        return ProtegoUserType.valueOf(this.userType);
    }

    public void setUserType(ProtegoUserType type) {
        this.userType = type.name();
    }

    @Override
    public String toString() {
        return "ProtegoUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    @Override
    public int compareTo(ProtegoUser user) {
        return getLastName().compareTo(user.getLastName());
    }
}
