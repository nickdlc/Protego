package com.example.protego.web;

public enum Endpoint {
    GET_PATIENT("patient"), GET_DOCTOR("doctor"),
    GET_QR_CODE("qrcode"), GET_MEDICALINFO("medicalInfo"),
    GET_DOCTORS_FOR_PATIENT("patientsAssignedTo"),

    POST_ASSIGN("assign"), POST_NOTE("note"),
    POST_MEDICALINFO("medicalInfo"), POST_MEDICATION("medication"),

    GEN_DATA("generatePatientInfo");

    private String endpointPath;
    Endpoint(String endpointName) {
        this.endpointPath = endpointName;
    }

    public String getEndpointPath() {
        return this.endpointPath;
    }
}
