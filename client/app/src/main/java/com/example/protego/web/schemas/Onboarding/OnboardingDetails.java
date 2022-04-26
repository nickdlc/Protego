package com.example.protego.web.schemas.Onboarding;

import java.util.Date;
import java.util.List;

public class OnboardingDetails {

    private String onboardingID;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String emergencyContactEmail;
    private String height;
    private Date dateCreated;
    private List<Allergy> allergiesList;
    private List<Surgery> surgeriesList;
    private List<Cancer> cancerList;
    private List<Diabetes> diabetesList;
    private List<OtherMedicalCondition> otherMedicalConditionsList;


    public String getOnboardingID() {
        return getOnboardingID();
    }

    public void setNoteID(String onboardingID) {
        this.onboardingID = onboardingID;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth= dateOfBirth;}

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyContactName() {
        return this.emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactNumber() {
        return this.emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public String getEmergencyContactEmail() {
        return this.emergencyContactEmail;
    }

    public void setEmergencyContactEmail(String emergencyContactEmail) {
        this.emergencyContactEmail = emergencyContactEmail;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String heightFeet, String heightInches) {
        this.height = heightFeet + "'" + heightInches;
    }


    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


    public List<Allergy> getAllergiesList() {
        return this.allergiesList;
    }

    public void setAllergiesList(List<Allergy> allergiesList) {
        this.allergiesList = allergiesList;
    }

    public List<Surgery> getSurgeriesList() {
        return this.surgeriesList;
    }

    public void setSurgeriesList(List<Surgery> surgeriesList) {
        this.surgeriesList = surgeriesList;
    }

    public List<Cancer> getCancerList() {
        return this.cancerList;
    }

    public void setCancerList(List<Cancer> cancerList) {
        this.cancerList = cancerList;
    }


    public List<Diabetes> getDiabetesList() {
        return this.diabetesList;
    }

    public void setDiabetesList(List<Diabetes> diabetesList) {
        this.diabetesList = diabetesList;
    }

    public List<OtherMedicalCondition> getOtherMedicalConditionsList() {
        return this.otherMedicalConditionsList;
    }

    public void setOtherMedicalConditionsList(List<OtherMedicalCondition> otherMedicalConditionsList) {
        this.otherMedicalConditionsList = otherMedicalConditionsList;
    }



    @Override
    public String toString() {
        return "OnboardingDetails{" +
                "onboardingID='" + onboardingID + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateOfBirth=" + dateOfBirth +
                ", email=" + email +
                ", phoneNumber=" + phoneNumber +
                ", emergencyContactName=" + emergencyContactName +
                ", emergencyContactNumber=" + emergencyContactNumber +
                ", emergencyContactEmail=" + emergencyContactEmail +
                ", height='" + height +
                ", allergiesList=" + allergiesList +
                ", surgeriesList=" + surgeriesList +
                ", cancerList=" + cancerList +
                ", diabetesList=" + diabetesList +
                ", otherMedicalConditionsList=" + otherMedicalConditionsList +

                '\'' +
                '}';
    }



}
