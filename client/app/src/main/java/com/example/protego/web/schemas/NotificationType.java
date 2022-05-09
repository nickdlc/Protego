package com.example.protego.web.schemas;

public enum NotificationType {
    CONNECTIONREQUEST("ConnectionRequest"),
    MEDICATIONREMINDER("MedicationReminder"),
    PRESCRIPTION("Prescription");

    private String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
