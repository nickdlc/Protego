package com.example.Protego.web;

import com.example.Protego.FirebaseAttributes.FirebaseAttributes;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class ProtegoHomeController {
    @RequestMapping("/")
    public String root() {
        return "hi";
    }

    @GetMapping("/user")
    public ProtegoUser getUser(@RequestParam("user") String uid) {
        // Get user with id uid
        try {
            // Temporarily create a new doctor and return that
            FirebaseAttributes.firestore.collection("users").document(uid).get();

            // Asynchronously retrieve multiple documents
            ApiFuture<DocumentSnapshot> future =
                    FirebaseAttributes.firestore.collection("users").document(uid).get();
            // future.get() blocks on response
            DocumentSnapshot document = future.get();

            return document.toObject(ProtegoUser.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/patient")
    public Patient getPatient(@RequestParam("patient") String puid) {
        // Get patient with id puid
        try {
            // Temporarily create a new doctor and return that
            FirebaseAttributes.firestore.collection("users").document(puid).get();

            // Asynchronously retrieve multiple documents
            ApiFuture<DocumentSnapshot> future =
                    FirebaseAttributes.firestore.collection("users").document(puid).get();
            // future.get() blocks on response
            DocumentSnapshot document = future.get();

            return document.toObject(Patient.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/doctor")
    public Doctor getDoctor(@RequestParam("doctor") String duid) {
        // Get doctor with id duid
        try {
            // Temporarily create a new doctor and return that
            FirebaseAttributes.firestore.collection("users").document(duid).get();

            // Asynchronously retrieve multiple documents
            ApiFuture<DocumentSnapshot> future =
                    FirebaseAttributes.firestore.collection("users").document(duid).get();
            // future.get() blocks on response
            DocumentSnapshot document = future.get();

            return document.toObject(Doctor.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/assign")
    public AssignedTo homie(@RequestBody AssignedTo pair) {
        /* Assign a patient to a doctor and return the pair */
        FirebaseAttributes.firestore.collection("AssignedTo").add(pair);

        return pair;
    }

    @PostMapping("/note")
    public Note postNote(@RequestBody Note note) {
        // Add note to Firestore and return it
        FirebaseAttributes.firestore.collection("users")
                .document(note.getCreator()).collection("Notes")
                .add(note);

        return note;
    }

    @GetMapping("/medicalInfo")
    public MedicalInfo getMedicalInfo(@RequestParam("patient") String puid) {
        try {
            // Asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future =
                    FirebaseAttributes.firestore.collection("users")
                                            .document(puid)
                                            .collection("MedicalInfo").get();
                                            //.orderBy("date", Query.Direction.DESCENDING).limit(1).get();

            // Just grab the top element for now
            DocumentSnapshot document = future.get().getDocuments().get(0);

            return document.toObject(MedicalInfo.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/medicalInfo")
    public MedicalInfo postMedicalInfo(@RequestBody MedicalInfo medInfo) {
        // Add medInfo to Firestore and return it
        FirebaseAttributes.firestore.collection("users")
                .document(medInfo.getPatient()).collection("MedicalInfo")
                .add(medInfo);

        return medInfo;
    }

    @PostMapping("/medication")
    public Medication postMedication(@RequestBody Medication medication) {
        // Add medication to Firestore and use it
        FirebaseAttributes.firestore.collection("users")
                        .document(medication.getPrescribee()).collection("Medication")
                        .add(medication);

        return medication;
    }

    @PostMapping("/generatePatientInfo")
    public MedicalInfo generatePatientInfo(@RequestBody Patient patient) {
        String puid = patient.getPatientID();
        MedicalInfo medInfo = new MedicalInfo();
        medInfo.setHeartRate(randNum());
        medInfo.setBloodPressure(randNum() + 40 + "/" + randNum());
        medInfo.setBloodType("A+");
        medInfo.setHeightIN(randNum() - 20);
        medInfo.setWeight(randNum() + 60);
        // Add medication to Firestore and use it
        FirebaseAttributes.firestore.collection("users")
                .document(puid).collection("MedicalInfo")
                .add(medInfo);

        return medInfo;
    }

    private Integer randNum() {
        return (int)Math.floor(Math.random() * 40) + 60;
    }
}
