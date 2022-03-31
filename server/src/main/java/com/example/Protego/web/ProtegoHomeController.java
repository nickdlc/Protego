package com.example.Protego.web;

import com.example.Protego.FirebaseAttributes.FirebaseAttributes;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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


//Generate random Patient Vital

    @PostMapping("/generateVital")
    public Vital generatePatientVital(@RequestBody Patient patient) {

        String puid = patient.getPatientID();
        Vital vital = new Vital();
        vital.setHeartRate(randNum());
        vital.setRespiratoryRate(randNumInRange(16, 25)); //a respiratory rate between 16-20 is normal, below 16 is abnormal and above 25 is abnormal
        double randFloat = 98.4;
        vital.setTemperature(randFloat);
        vital.setPatientID(puid);
        vital.setBloodPressure(Integer.toString(randNumInRange(90, 119)) + "/" +  Integer.toString(randNumInRange(60, 79))); //a blood pressure systolic < 120 or a distolic rate < 80 are considered normal
        vital.setSource(randomSource());
        Date date = new Date();
        vital.setDate(date);
        // Add vitals to Firestore and use it
        FirebaseAttributes.firestore.collection("users")
                .document(puid).collection("Vitals")
                .add(vital);


        return vital;
    }


    @GetMapping("/Vital")
    public List<Vital> getVitals(@RequestParam("patient") String puid) {
        try {


            // Asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future =
                    FirebaseAttributes.firestore.collection("users")
                            .document(puid)
                            .collection("Vitals").orderBy("date", Query.Direction.DESCENDING).get();


            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Vital> VitalArray= new ArrayList<Vital>();

            for (QueryDocumentSnapshot document : documents) {
                VitalArray.add(document.toObject(Vital.class));
            }


            return VitalArray;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    List<String> randomDoctors = randomApprovedDoctors();

    //generate a random note
    @PostMapping("/generatePatientNote")
    public Note generatePatientNote(@RequestBody Patient patient) {
        String puid = patient.getPatientID();
        Note note = new Note();
        note.setCreator(puid);
        int rand = randNumInRange(1, 10);
        note.setTitle("Note #" + Integer.toString(rand));
        Date date = new Date();
        note.setDateCreated(date); //compute this date
        note.setContent(randomMessage());
        note.setVisibility(randomVisibility());
        note.setApprovedDoctors(randomDoctors);
        //note.setApprovedDoctors(randomApprovedDoctors());
        FirebaseAttributes.firestore.collection("users")
                .document(puid).collection("Notes")
                .add(note);
        return note;
    }

    //get the notes
    @GetMapping("/note")
    public List<Note> getNotes(@RequestParam("patient") String puid) {
        try {
            // Asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future =
                    FirebaseAttributes.firestore.collection("users")
                            .document(puid)
                            .collection("Notes").orderBy("dateCreated", Query.Direction.DESCENDING).get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Note> NotesArray= new ArrayList<Note>();

            for (QueryDocumentSnapshot document : documents) {
                NotesArray.add(document.toObject(Note.class));
            }


            return NotesArray;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }


    //generate medication data
    @PostMapping("/generatePatientMedication")
    public Medication generatePatientMedication(@RequestBody Patient patient) {
        String puid = patient.getPatientID();
        Medication medication = new Medication();
        List<String> medicationResults = randomMedication(randomDoctors);
        //List<String> medicationResults = randomMedication(randomApprovedDoctors());
        medication.setName(medicationResults.get(0));
        medication.setPrescribee(puid);
        medication.setPrescriber(medicationResults.get(2));
        Date date = new Date();
        medication.setDatePrescribed(date); //compute this date
        medication.setDosage(medicationResults.get(1));
        // Add medication to Firestore and use it
        FirebaseAttributes.firestore.collection("users")
                .document(puid).collection("Medications")
                .add(medication);
        return medication;
    }

    //get the medication data
    @GetMapping("/medication")
    public List<Medication> getMedications(@RequestParam("patient") String puid) {
        try {
            // Asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future =
                    FirebaseAttributes.firestore.collection("users")
                            .document(puid)
                            .collection("Medications").orderBy("datePrescribed", Query.Direction.DESCENDING).get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Medication> MedicationArray= new ArrayList<Medication>();

            for (QueryDocumentSnapshot document : documents) {
                MedicationArray.add(document.toObject(Medication.class));
            }


            return MedicationArray;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }





    @GetMapping("/patientsAssignedTo")
    public List<Patient> getPatientsForDoctor(@RequestParam("doctor") String duid) {
        try {
            // Asynchronously retrieve multiple documents
            ApiFuture<QuerySnapshot> future =
                    FirebaseAttributes.firestore.collection("AssignedTo")
                            .whereEqualTo("doctor", duid)
                            .get();
            //.orderBy("date", Query.Direction.DESCENDING).limit(1).get();

            // Just grab the top element for now
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            return documents.stream()
                    .map(qds -> // results -> Patient IDs
                            qds.toObject(AssignedTo.class).getPatient()).collect(Collectors.toList()).stream()
                    .map(puid -> // Patient IDs -> api calls
                        FirebaseAttributes.firestore.collection("users")
                                .document(puid).get()).collect(Collectors.toList()).stream()
                    .map(snapshot -> { // api calls -> Patients
                        try {
                            return snapshot.get().toObject(Patient.class);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Integer randNum() {
        return (int)Math.floor(Math.random() * 40) + 60;
    }


    private Integer randNumInRange(Integer min, Integer max) {  //the min and max values are inclusive
        Random random = new Random();
        Integer aboveMax = max + 1;
        int randint = random.nextInt(aboveMax);

        while (randint < min || randint > aboveMax) {
            if (randint < min) {
                randint = randint + min;
            } else if (randint > aboveMax) {
                randint = randint % aboveMax;
            }
        }
        return randint;
    }

    private String randomSource(){
        int num = randNumInRange(0, 1);
        String[] sourceTypes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        String source = "";
        if(num == 0){
            source = "I";
        }
        else if(num == 1){
            source = "Dr. " + sourceTypes[randNumInRange(0, sourceTypes.length - 1)];
        }
        else{
            source = "I";
        }
        return source;
    }



    private List<String> randomApprovedDoctors(){

        int rand = randNumInRange(1, 9);
        String[] sourceTypes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        List<String> result = new ArrayList<String>();
        String source;

        for(int i = 0; i < rand; i++){
            result.add("Dr. " + sourceTypes[i]);
        }

        return result;
    }



    private String randomMessage(){

        int rand = randNumInRange(0, 5);
        String[] messages = {
               "I have intense back pain",
               "I have an intense headache",
               "I had a bad reaction to my medication",
               "I have a new rash throughout my body",
               "My blood pressure is higher than usual",
               "My temperature is very high"
        };

        return messages[rand];
    }

    private String randomVisibility(){
        String visibility;
        int rand = randNumInRange(0, 1);
        if(rand == 0){
            visibility = "Public";
        }
        else{
            visibility = "Private";
        }

        return visibility;
    }


    private List<String> randomMedication(List<String> approvedDoctors){
        String[] name = {"Atorvastatin", "Omeprazole", "Amlodipine", "Lisinopril", "Levothyroxine", "Metformin"};
        int randNameIndex = randNumInRange(0, name.length - 1);
        String nameSelected = name[randNameIndex];
        int randDoctorsIndex = randNumInRange(0, approvedDoctors.size() - 1);
        List<String> dosages = new ArrayList<String>();
        String dosage = "";
        List<String> medicationInfo = new ArrayList<String>();
        String units = "mg";

        if(nameSelected == name[0]){
            dosages.clear();
            Collections.addAll(dosages, "10", "20", "40", "80");
            units = "mg";

        } else if(nameSelected == name[1]){
            dosages.clear();
            Collections.addAll(dosages, "10", "20", "40");
            units = "mg";


        } else if(nameSelected == name[2]){
            dosages.clear();
            Collections.addAll(dosages, "2.5", "5", "10");
            units = "mg";



        } else if(nameSelected == name[3]){
            dosages.clear();
            Collections.addAll(dosages, "2.5", "5", "10", "20", "30", "40");
            units = "mg";

        }
        else if(nameSelected == name[4]){
            dosages.clear();
            Collections.addAll(dosages, "25", "50", "75", "88", "100", "112", "125", "137", "150", "175", "200", "300");
            units = "mcg";

        }
        else if(nameSelected == name[5]){
            dosages.clear();
            Collections.addAll(dosages, "500", "850", "1000");
            units = "mg";

        }

        int randDosageIndex = randNumInRange(0, dosages.size() - 1);
        dosage = dosages.get(randDosageIndex);

        String prescriber = approvedDoctors.get(randDoctorsIndex);

        Collections.addAll(medicationInfo, nameSelected, dosage + units, prescriber);

        return medicationInfo;
    }



}
