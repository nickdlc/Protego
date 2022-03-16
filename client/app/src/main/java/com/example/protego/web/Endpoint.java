package com.example.protego.web;

public enum Endpoint {
    GET_PATIENT("patient"), GET_DOCTOR("doctor"),
    GET_QR_CODE("qrCode"),

    POST_ASSIGN("assign"), POST_NOTE("note"),
    POST_MEDICALINFO("medicalinfo"), POST_MEDICATION("medication");

    private String endpointPath;
    Endpoint(String endpointName) {
        this.endpointPath = endpointName;
    }

    public String getEndpointPath() {
        return this.endpointPath;
    }
}
