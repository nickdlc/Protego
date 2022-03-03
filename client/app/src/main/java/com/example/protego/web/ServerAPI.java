package com.example.protego.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerAPI {
    public static void getPatient(String puid, ServerRequestListener serverRequestListener) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("patient", puid);
        new ServerRequest(Endpoint.GET_PATIENT)
                .get(urlParams, serverRequestListener);
    }

    public static void getDoctor(String duid, ServerRequestListener serverRequestListener) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("doctor", duid);
        new ServerRequest(Endpoint.GET_DOCTOR)
                .get(urlParams, serverRequestListener);
    }

    public static void createNote(String creator,
                           List<String> approvedDoctors,
                           String content,
                           ServerRequestListener serverRequestListener) {
        // Convert the current date to format `yyyy-MM-dd'T'HH:mm'Z'`
        String date = getCurrentFormmatedDate();

        Map<String, Object> params = new HashMap<>();
        params.put("noteID", getRandomUid());
        params.put("creator", creator);
        params.put("dateCreated", date);
        params.put("approvedDoctors", approvedDoctors);
        params.put("content", content);

        new ServerRequest(Endpoint.POST_NOTE)
                .post(params, serverRequestListener);
    }

    public static void createMedicalInfo(String puid,
                                  String healthInsuranceNumber,
                                  String sex,
                                  String bloodType,
                                  int heightIN,
                                  int weight,
                                  int heartRate,
                                  String bloodPressure,
                                  ServerRequestListener serverRequestListener) {
        // Convert the current date to format `yyyy-MM-dd'T'HH:mm'Z'`
        String date = getCurrentFormmatedDate();

        Map<String, Object> params = new HashMap<>();
        params.put("infoID", getRandomUid());
        params.put("puid", puid);
        params.put("healthInsuranceNumber", healthInsuranceNumber);
        params.put("sex", sex);
        params.put("bloodType", bloodType);
        params.put("heightIN", heightIN);
        params.put("weight", weight);
        params.put("heartRate", heartRate);
        params.put("bloodPressure", bloodPressure);

        new ServerRequest(Endpoint.POST_MEDICALINFO)
                .post(params, serverRequestListener);
    }

    public static void createMedication(String prescribee,
                                 Map<String, String> dosage,
                                 String prescriber,
                                 ServerRequestListener serverRequestListener) {
        // Convert the current date to format `yyyy-MM-dd'T'HH:mm'Z'`
        String date = getCurrentFormmatedDate();

        Map<String, Object> params = new HashMap<>();
        params.put("medID", getRandomUid());
        params.put("precribee", prescribee);
        params.put("datePrescribed", date);
        params.put("dosage", dosage);
        params.put("prescriber", prescriber);

        new ServerRequest(Endpoint.POST_MEDICATION)
                .post(params, serverRequestListener);
    }

    public static String getCurrentFormmatedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        Date currentDate = new Date(System.currentTimeMillis());
        return formatter.format(currentDate);
    }

    public static String getRandomUid() {
        return String.valueOf(Math.floor(Math.random() * 400));
    }
}