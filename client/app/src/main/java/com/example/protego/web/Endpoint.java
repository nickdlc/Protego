package com.example.protego.web;

@Deprecated
public enum Endpoint {
    GET_PATIENT("patient"), GET_DOCTOR("doctor"),
    GET_QR_CODE("qrcode"), GET_MEDICALINFO("medicalInfo"),
    GET_DOCTORS_FOR_PATIENT("patientsAssignedTo"),

    POST_ASSIGN("assign"), POST_NOTE("note"),
    POST_MEDICALINFO("medicalInfo"), POST_MEDICATION("medication"),

    GEN_DATA("generatePatientInfo"),

    GEN_VITAL_DATA("generateVital"),
    GET_VITALS("Vital"),


    GEN_NOTE_DATA("generatePatientNote"),
    GET_NOTES("note"),

    GEN_MEDICATION_DATA("generatePatientMedication"),
    GET_MEDICATIONS("medication");






    private String endpointPath;
    Endpoint(String endpointName) {
        this.endpointPath = endpointName;
    }

    public String getEndpointPath() {
        return this.endpointPath;
    }
}
