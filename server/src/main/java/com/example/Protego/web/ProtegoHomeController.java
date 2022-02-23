package com.example.Protego.web;

import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ProtegoHomeController {
    @RequestMapping("/")
    public String root() {
        return "hi";
    }

    @GetMapping("/user")
    public ProtegoUser getUser() {
        // Temporarily create a new user and return that
        ProtegoUser user = new ProtegoUser();
        user.setId("12");
        user.setFirstName("spring");
        user.setLastName("test");

        return user;
    }

    @GetMapping("/patient")
    public Patient getPatient() {
        // Temporarily create a new patient and return that
        Patient patient = new Patient();
        patient.setId("13");
        patient.setPatientID("p13");
        patient.setFirstName("patient");
        patient.setLastName("test");

        return patient;
    }

    @GetMapping("/doctor")
    public Doctor getDoctor() {
        // Temporarily create a new doctor and return that
        Doctor doctor = new Doctor();
        doctor.setId("14");
        doctor.setDoctorID("d14");
        doctor.setFirstName("doctor");
        doctor.setLastName("test");
        doctor.setAddress("city college's address");
        doctor.setSpecialty("optometry");

        return doctor;
    }

    @PostMapping("/assign")
    public AssignedTo homie(@RequestBody AssignedTo pair) {
        /* Assign a patient to a doctor */
        AssignedTo newPair = new AssignedTo();
        newPair.setUid(pair.getUid());
        newPair.setPatient(pair.getPatient());
        newPair.setDoctor(pair.getDoctor());
        newPair.setActive(pair.getActive());

        return newPair;
    }

    @PostMapping("/note")
    public Note postNote(@RequestBody Note note) {
        // Create a new note and return it
        Note newNote = new Note();
        System.out.println(note);
        newNote.setNoteID(note.getNoteID());
        newNote.setCreator(note.getCreator());
        newNote.setDateCreated(note.getDateCreated());
        newNote.setApprovedDoctors(note.getApprovedDoctors());
        newNote.setContent(note.getContent());

        return newNote;
    }

    @PostMapping("/medicalInfo")
    public MedicalInfo postMedicalInfo(@RequestBody MedicalInfo medInfo) {
        // Create a new medical info record and return it
        MedicalInfo mi = new MedicalInfo();
        mi.setInfoID(medInfo.getInfoID());
        mi.setPatient(medInfo.getPatient());
        mi.setLastUpdated(medInfo.getLastUpdated());
        mi.setHealthInsuranceNumber(medInfo.getHealthInsuranceNumber());
        mi.setSex(medInfo.getSex());
        mi.setBloodType(medInfo.getBloodType());
        mi.setHeightIN(medInfo.getHeightIN());
        mi.setWeight(medInfo.getWeight());
        mi.setHeartRate(medInfo.getHeartRate());
        mi.setBloodPressure(medInfo.getBloodPressure());

        return mi;
    }

    @PostMapping("/medication")
    public Medication postMedication(@RequestBody Medication medication) {
        // Create a new medication record and return it
        Medication med = new Medication();
        System.out.println(medication);
        med.setMedID(medication.getMedID());
        med.setPrescribee(medication.getPrescribee());
        med.setDatePrescribed(medication.getDatePrescribed());
        med.setDosage(medication.getDosage());
        med.setPrescriber(medication.getPrescriber());

        return med;
    }
}
