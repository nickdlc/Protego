package com.example.protego.web.schemas.Onboarding;

import com.example.protego.NewAllergyFragment;
import com.example.protego.NewCancerFragment;
import com.example.protego.NewDiabetesFragment;
import com.example.protego.NewOtherMedicalConditionsFragment;
import com.example.protego.NewSurgeryFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnboardingInfo {

    private Date dateCreated;
    private String userID;
    private final String DOB;
    private final String Email;
    private final String PhoneNumber;
    private final String Home_Address;
    private final String Emergency_Name;
    private final String Emergency_PhoneNumber;
    private final String Emergency_Email;
    private final String Height;
    private ArrayList<NewAllergyFragment.AllergyInfo> allergyData = new ArrayList<>();
    private ArrayList<NewSurgeryFragment.SurgeryInfo> surgeryData = new ArrayList<>();
    private ArrayList<NewCancerFragment.CancerInfo> cancerData = new ArrayList<>();
    private ArrayList<NewDiabetesFragment.DiabetesInfo> diabetesData = new ArrayList<>();
    private ArrayList<NewOtherMedicalConditionsFragment.OtherConditionsInfo> otherConditionsData = new ArrayList<>();


    public OnboardingInfo(String userID, String DOB, String Email, String PhoneNumber, String Home_Address, String Emergency_Name,
                          String Emergency_PhoneNumber, String Emergency_Email, String Height,
                          ArrayList<NewAllergyFragment.AllergyInfo> allergyData,
                          ArrayList<NewSurgeryFragment.SurgeryInfo> surgeryData,
                          ArrayList<NewCancerFragment.CancerInfo> cancerData,
                          ArrayList<NewDiabetesFragment.DiabetesInfo> diabetesData,
                          ArrayList<NewOtherMedicalConditionsFragment.OtherConditionsInfo> otherConditionsData

    ) {
        this.userID = userID;
        this.DOB = DOB;
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;
        this.Home_Address = Home_Address;
        this.Emergency_Name = Emergency_Name;
        this.Emergency_PhoneNumber = Emergency_PhoneNumber;
        this.Emergency_Email = Emergency_Email;
        this.Height = Height;
        this.allergyData = allergyData;
        this.surgeryData = surgeryData;
        this.cancerData = cancerData;
        this.diabetesData = diabetesData;
        this.otherConditionsData = otherConditionsData;

    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {this.userID = userID;}

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date date) {this.dateCreated = date;}

    public String getDateOfBirth() {
        return this.DOB;
    }

    public String getEmail() {
        return this.Email;
    }

    public String getPhone() {
        return this.PhoneNumber;
    }

    public String getHomeAddress() {
        return this.Home_Address;
    }

    public String getEmergencyName() {
        return this.Emergency_Name;
    }

    public String getEmergencyPhoneNumber() {
        return this.Emergency_PhoneNumber;
    }
    public String getEmergencyEmail() {
        return this.Emergency_Email;
    }
    public String getHeight() {
        return this.Height;
    }

    public ArrayList<NewAllergyFragment.AllergyInfo> getAllergyData() {
        return this.allergyData;
    }
    public ArrayList<NewSurgeryFragment.SurgeryInfo> getSurgeryData() {
        return this.surgeryData;
    }
    public ArrayList<NewCancerFragment.CancerInfo> getCancerData() {
        return this.cancerData;
    }
    public ArrayList<NewDiabetesFragment.DiabetesInfo> getDiabetesData() {
        return this.diabetesData;
    }
    public ArrayList<NewOtherMedicalConditionsFragment.OtherConditionsInfo> getOtherConditionsData() {
        return this.otherConditionsData;
    }

}