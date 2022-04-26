package com.example.protego.web.schemas;

public class Doctor extends ProtegoUser {
    private String doctorID;
    private String workplaceName;
    private String address;
    private String specialty;

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return this.specialty;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorID='" + doctorID + '\'' +
                ", workplaceName='" + workplaceName + '\'' +
                ", address='" + address + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }
}
