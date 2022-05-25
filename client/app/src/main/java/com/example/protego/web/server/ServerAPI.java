package com.example.protego.web.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* USE FirestoreAPI INSTEAD */
@Deprecated
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

    public static void getMedication(String puid, ServerRequestListener serverRequestListener) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("patient", puid);

        new ServerRequest(Endpoint.GET_MEDICALINFO)
                .get(urlParams, serverRequestListener);
    }

    public static void generateMedData(String puid, ServerRequestListener serverRequestListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("patientID", puid);

        new ServerRequest(Endpoint.GEN_DATA)
                .post(params, serverRequestListener);
    }


    public static void getDoctorAssignedPatients(String duid, ServerRequestListener serverRequestListener) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("doctor", duid);

        new ServerRequest(Endpoint.GET_DOCTORS_FOR_PATIENT)
                .get(urlParams, serverRequestListener);
    }


//GENERATE REQUESTS

    //generate random vital data
    public static void generateVitalData(String puid, ServerRequestListener serverRequestListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("patientID", puid);

        new ServerRequest(Endpoint.GEN_VITAL_DATA)
                .post(params, serverRequestListener);
    }


    //generate random note data
    public static void generateNoteData(String puid, ServerRequestListener serverRequestListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("patientID", puid);

        new ServerRequest(Endpoint.GEN_NOTE_DATA)
                .post(params, serverRequestListener);
    }



//GET REQUESTS

    //get all vital data
    public static void getVitals(String puid, ServerRequestListener serverRequestListener) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("patient", puid);

        new ServerRequest(Endpoint.GET_VITALS)
                .get(urlParams, serverRequestListener);
    }


    //get all note data
    public static void getNotes(String puid, ServerRequestListener serverRequestListener) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("patient", puid);

        new ServerRequest(Endpoint.GET_NOTES)
                .get(urlParams, serverRequestListener);
    }


    //generate random medication data
    public static void generateMedicationData(String puid, ServerRequestListener serverRequestListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("patientID", puid);

        new ServerRequest(Endpoint.GEN_MEDICATION_DATA)
                .post(params, serverRequestListener);
    }

    //get all medication data
    public static void getMedications(String puid, ServerRequestListener serverRequestListener) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("patient", puid);

        new ServerRequest(Endpoint.GET_MEDICATIONS)
                .get(urlParams, serverRequestListener);
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