package com.example.protego.web.schemas;

public enum NotificationType {
    CONNECTIONREQUEST("ConnectionRequest"),
    MEDICATIONREMIDNER("MedicationReminder");

    private String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
