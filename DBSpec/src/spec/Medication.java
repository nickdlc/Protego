package spec;

import java.util.Date;

public abstract class Medication {
    private String medID;
    private Patient prescribee;
    private Date datePrescribed;
    private String dosage;
    private Doctor prescriber;

    public String getMedID() {
        return medID;
    }

    public void setMedID(String medID) {
        this.medID = medID;
    }

    public Patient getPrescribee() {
        return prescribee;
    }

    public void setPrescribee(Patient prescribee) {
        this.prescribee = prescribee;
    }

    public Date getDatePrescribed() {
        return datePrescribed;
    }

    public void setDatePrescribed(Date date) {
        this.datePrescribed = datePrescribed;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Doctor getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(Doctor prescriber) {
        this.prescriber = prescriber;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "medID='" + medID + '\'' +
                ", prescribee=" + prescribee +
                ", date=" + datePrescribed +
                ", dosage='" + dosage + '\'' +
                ", prescriber=" + prescriber +
                '}';
    }
}
