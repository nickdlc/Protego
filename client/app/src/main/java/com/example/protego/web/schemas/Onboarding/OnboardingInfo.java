package com.example.protego.web.schemas.Onboarding;

import com.example.protego.NewAllergyFragment;
import com.example.protego.NewCancerFragment;
import com.example.protego.NewDiabetesFragment;
import com.example.protego.NewOtherMedicalConditionsFragment;
import com.example.protego.NewSurgeryFragment;

import java.util.ArrayList;
import java.util.Date;

public class OnboardingInfo {

    private Date dateCreated;
    private String userID;
    private final String DOB;
    private final String PhoneNumber;
    private final String Home_Address;
    private final String Emergency_Name;
    private final String Emergency_PhoneNumber;
    private final String Emergency_Email;
    private final String Height;
    private final String Weight;
    private ArrayList<AllergyInfo> allergyData = new ArrayList<>();
    private ArrayList<SurgeryInfo> surgeryData = new ArrayList<>();
    private ArrayList<CancerInfo> cancerData = new ArrayList<>();
    private ArrayList<DiabetesInfo> diabetesData = new ArrayList<>();
    private ArrayList<OtherConditionsInfo> otherConditionsData = new ArrayList<>();


    public OnboardingInfo(String userID, String DOB, String PhoneNumber, String Home_Address, String Emergency_Name,
                          String Emergency_PhoneNumber, String Emergency_Email, String Height, String Weight,
                          ArrayList<AllergyInfo> allergyData,
                          ArrayList<SurgeryInfo> surgeryData,
                          ArrayList<CancerInfo> cancerData,
                          ArrayList<DiabetesInfo> diabetesData,
                          ArrayList<OtherConditionsInfo> otherConditionsData

    ) {
        this.userID = userID;
        this.DOB = DOB;
        this.PhoneNumber = PhoneNumber;
        this.Home_Address = Home_Address;
        this.Emergency_Name = Emergency_Name;
        this.Emergency_PhoneNumber = Emergency_PhoneNumber;
        this.Emergency_Email = Emergency_Email;
        this.Height = Height;
        this.Weight = Weight;
        this.allergyData = allergyData;
        this.surgeryData = surgeryData;
        this.cancerData = cancerData;
        this.diabetesData = diabetesData;
        this.otherConditionsData = otherConditionsData;

    }

    public OnboardingInfo() {
        this.userID = null;
        this.DOB = null;
        this.PhoneNumber = null;
        this.Home_Address = null;
        this.Emergency_Name = null;
        this.Emergency_PhoneNumber = null;
        this.Emergency_Email = null;
        this.Height = null;
        this.Weight = null;


    }

    public OnboardingInfo(OnboardingInfo item){
        this.userID = item.userID;
        this.DOB = item.DOB;
        this.PhoneNumber = item.PhoneNumber;
        this.Home_Address = item.Home_Address;
        this.Emergency_Name = item.Emergency_Name;
        this.Emergency_PhoneNumber = item.Emergency_PhoneNumber;
        this.Emergency_Email = item.Emergency_Email;
        this.Height = item.Height;
        this.Weight = item.Weight;
        this.allergyData = item.allergyData;
        this.surgeryData = item.surgeryData;
        this.cancerData = item.cancerData;
        this.diabetesData = item.diabetesData;
        this.otherConditionsData = item.otherConditionsData;
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
    public String getWeight() {
        return this.Weight;
    }

    public ArrayList<AllergyInfo> getAllergyData() {
        return this.allergyData;
    }
    public ArrayList<SurgeryInfo> getSurgeryData() {
        return this.surgeryData;
    }
    public ArrayList<CancerInfo> getCancerData() {
        return this.cancerData;
    }
    public ArrayList<DiabetesInfo> getDiabetesData() {
        return this.diabetesData;
    }
    public ArrayList<OtherConditionsInfo> getOtherConditionsData() {
        return this.otherConditionsData;
    }

}