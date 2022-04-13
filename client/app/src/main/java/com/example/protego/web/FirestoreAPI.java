package com.example.protego.web;

import static com.example.protego.util.RandomGenerator.randNum;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import com.example.protego.web.schemas.MedicalInfo;
import com.example.protego.util.RandomGenerator;
import com.example.protego.web.schemas.AssignedTo;
import com.example.protego.web.schemas.Doctor;
import com.example.protego.web.schemas.Medication;
import com.example.protego.web.schemas.Note;
import com.example.protego.web.schemas.Patient;
import com.example.protego.web.schemas.Vital;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class FirestoreAPI {
    public static final String TAG = "FirestoreAPI";
    private static FirestoreAPI instance = null;

    private FirebaseFirestore db;

    private FirestoreAPI() {
        db = FirebaseFirestore.getInstance();
    }

    public synchronized static FirestoreAPI getInstance() {
        if (instance == null)
            return instance = new FirestoreAPI();

        return instance;
    }

    public Task<DocumentSnapshot> getPatient(String puid, FirestoreListener<Patient> listener) {
        return db.collection("users")
                .document(puid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        simpleLinkTaskToListener(task, Patient.class, listener, "Failed to get patient...");
                    }
                });
    }

    public Task<DocumentSnapshot> getDoctor(String duid, FirestoreListener<Doctor> listener) {
        return db.collection("users")
                .document(duid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        simpleLinkTaskToListener(task, Doctor.class, listener, "Failed to get doctor...");
                    }
                });
    }

    public Task<DocumentReference> createNote(String creator,
                                              List<String> approvedDoctors,
                                              String content,
                                              FirestoreListener<Task> listener) {
        // Convert the current date to format `yyyy-MM-dd'T'HH:mm'Z'`
        String date = getCurrentFormmatedDate();

        Map<String, Object> params = new HashMap<>();
        params.put("creator", creator);
        params.put("dateCreated", date);
        params.put("approvedDoctors", approvedDoctors);
        params.put("content", content);

        return db.collection("users")
                .document(creator).collection("Notes")
                .add(params)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to create note..."));
    }

    public Task<DocumentReference> createNote(String creator,
                                              List<String> approvedDoctors,
                                              String content) {
        return createNote(creator, approvedDoctors, content, null);
    }

    public Task<DocumentReference> createMedicalInfo(String puid,
                                      String healthInsuranceNumber,
                                      String sex,
                                      String bloodType,
                                      int heightIN,
                                      int weight,
                                      int heartRate,
                                      String bloodPressure,
                                      FirestoreListener<Task> listener) {
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

        return db.collection("users")
                .document(puid).collection("MedicalInfo")
                .add(params)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to create medical info..."));
    }

    public Task<DocumentReference> createMedicalInfo(String puid,
                                                     String healthInsuranceNumber,
                                                     String sex,
                                                     String bloodType,
                                                     int heightIN,
                                                     int weight,
                                                     int heartRate,
                                                     String bloodPressure) {
        return createMedicalInfo(puid, healthInsuranceNumber, sex, bloodType, heightIN, weight, heartRate, bloodPressure, null);
    }



    public Task<DocumentReference> createMedication(String prescribee,
                                 Map<String, String> dosage,
                                 String prescriber,
                                 FirestoreListener<Task> listener) {
        // Convert the current date to format `yyyy-MM-dd'T'HH:mm'Z'`
        String date = getCurrentFormmatedDate();

        Map<String, Object> params = new HashMap<>();
        params.put("medID", getRandomUid());
        params.put("precribee", prescribee);
        params.put("datePrescribed", date);
        params.put("dosage", dosage);
        params.put("prescriber", prescriber);

        return db.collection("users")
                .document(prescribee).collection("Medication")
                .add(params)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to create medical info..."));
    }

    public Task<DocumentReference> createMedication(String prescribee,
                                                    Map<String, String> dosage,
                                                    String prescriber) {
        return createMedication(prescribee, dosage, prescriber, null);
    }

    public Task<QuerySnapshot> getMedicalInfo(String puid, FirestoreListener<MedicalInfo> listener) {
        Map<String, String> urlParams = new HashMap<>();

        return db.collection("users")
                .document(puid)
                .collection("MedicalInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get the first element for now
                            MedicalInfo medInfo = task.getResult().getDocuments().get(0).toObject(MedicalInfo.class);
                            listener.getResult(medInfo);
                        } else {
                            listener.getError(task.getException(), "Failed to get medical info for puid = ");
                        }
                    }
                });
    }

    public Task<DocumentReference> generateMedData(String puid, FirestoreListener listener) {
        MedicalInfo medInfo = new MedicalInfo();
        medInfo.setBloodPressure(randNum() + 40 + "/" + randNum());
        medInfo.setBloodType("A+");
        medInfo.setHeightIN(randNum() - 20);
        medInfo.setWeight(randNum() + 60);
        medInfo.setHeartRate(randNum());
        // Add medication to Firestore and use it
        return db.collection("users")
                .document(puid).collection("MedicalInfo")
                .add(medInfo)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to generate med data..."));
    }


    public void getDoctorAssignedPatients(String duid,
                                          FirestoreListener<List<Patient>> listener) {
        // Asynchronously retrieve multiple documents
        db.collection("AssignedTo")
                .whereEqualTo("doctor", duid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get the assignedto list and then get the patients
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            int totalPatients = documents.size();
                            List<Patient> patients = new Vector<>();
                            documents.stream()
                                    .map(qds -> // results -> Patient IDs
                                        qds.toObject(AssignedTo.class).getPatient()).collect(Collectors.toList()).stream()
                                    .map(puid -> {// Patient IDs -> patients
                                            return db.collection("users")
                                                    .document(puid.toString())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                Patient p = task.getResult().toObject(Patient.class);
                                                                patients.add(p);
                                                                if (patients.size() >= totalPatients) {
                                                                    listener.getResult(patients);
                                                                }
                                                                Log.d(TAG, "got pat " + p.getFirstName());
                                                            } else {
                                                                Log.e(TAG, "Failed to get information for patient for doctor", task.getException());
                                                            }
                                                        }
                                                    });
                                    }).collect(Collectors.toList());
                        } else {
                            Log.e(TAG, "Failed to get AssignedTo collection for doctor uid " + duid, task.getException());
                        }
                    }
                });
    }


//GENERATE REQUESTS

    //generate random vital data
    public void generateVitalData(String puid, FirestoreListener<Task> listener) {
        Vital vital = new Vital();
        vital.setHeartRate(randNum());
        vital.setRespiratoryRate(RandomGenerator.randNumInRange(16, 25)); //a respiratory rate between 16-20 is normal, below 16 is abnormal and above 25 is abnormal
        double randFloat = 98.4;
        vital.setTemperature(randFloat);
        vital.setPatientID(puid);
        vital.setBloodPressure(Integer.toString(RandomGenerator.randNumInRange(90, 119)) + "/" +  Integer.toString(RandomGenerator.randNumInRange(60, 79))); //a blood pressure systolic < 120 or a distolic rate < 80 are considered normal
        vital.setSource(RandomGenerator.randomSource());
        Date date = new Date();
        vital.setDate(date);
        // Add vitals to Firestore and use it
        db.collection("users")
                .document(puid).collection("Vitals")
                .add(vital)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to generate vital data..."));
    }


    //generate random note data
    public void generateNoteData(String puid, List<String> doctors, FirestoreListener<Task> listener) {
        Note note = new Note();
        note.setCreator(puid);
        int rand = RandomGenerator.randNumInRange(1, 10);
        note.setTitle("Note #" + Integer.toString(rand));
        Date date = new Date();
        note.setDateCreated(date); //compute this date
        note.setContent(RandomGenerator.randomMessage());
        note.setVisibility(RandomGenerator.randomVisibility());
        note.setApprovedDoctors(doctors);
        //note.setApprovedDoctors(randomApprovedDoctors());
        db.collection("users")
                .document(puid).collection("Notes")
                .add(note)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to generate note data..."));
    }



//GET REQUESTS

    //get all vital data
    public void getVitals(String puid, FirestoreListener<List<Vital>> listener) {
        // Asynchronously retrieve multiple documents
        db.collection("users")
                .document(puid)
                .collection("Vitals")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Vital> vitalList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                vitalList.add(document.toObject(Vital.class));
                            }
                            listener.getResult(vitalList);
                        } else {
                            listener.getError(task.getException(), "Failed to get vitals for puid = " + puid);
                        }
                    }
                });
    }


    //get all note data
    public void getNotes(String puid, FirestoreListener<List<Note>> listener) {
        // Asynchronously retrieve multiple documents
        db.collection("users")
                .document(puid)
                .collection("Notes")
                .orderBy("dateCreated", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Note> noteList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                noteList.add(document.toObject(Note.class));
                            }
                            listener.getResult(noteList);
                        } else {
                            listener.getError(task.getException(), "Failed to get notes for puid = " + puid);
                        }
                    }
                });
    }


    //generate random medication data
    public void generateMedicationData(String puid, List<String> doctors, FirestoreListener listener) {
        Medication medication = new Medication();
        List<String> medicationResults = RandomGenerator.randomMedication(doctors);
        //List<String> medicationResults = randomMedication(randomApprovedDoctors());
        medication.setName(medicationResults.get(0));
        medication.setPrescribee(puid);
        medication.setPrescriber(medicationResults.get(2));
        Date date = new Date();
        medication.setDatePrescribed(date); //compute this date
        medication.setDosage(medicationResults.get(1));
        // Add medication to Firestore and use it
        db.collection("users")
                .document(puid).collection("Medications")
                .add(medication)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to generate medication data..."));
    }

    //get all medication data
    public void getMedications(String puid, FirestoreListener<List<Medication>> listener) {

        // Asynchronously retrieve multiple documents
        db.collection("users")
                .document(puid)
                .collection("Medications")
                .orderBy("datePrescribed", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Medication> medicationArray = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                medicationArray.add(document.toObject(Medication.class));
                            }
                            listener.getResult(medicationArray);
                        } else {
                            listener.getError(task.getException(), "Failed to get vitals for puid = " + puid);
                        }
                    }
                });;
    }




    public static String getCurrentFormmatedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        Date currentDate = new Date(System.currentTimeMillis());
        return formatter.format(currentDate);
    }

    public static String getRandomUid() {
        return String.valueOf(Math.floor(Math.random() * 400));
    }

    private void simpleLinkTaskToListener(Task<DocumentSnapshot> task, Class cast, FirestoreListener listener, String errorMsg) {
        if (task.isSuccessful())
            listener.getResult(task.getResult().toObject(cast));
        else
            listener.getError(task.getException(), errorMsg);
    }

    private OnCompleteListener<DocumentReference> getListenerForCreation(FirestoreListener<Task> listener, String errorMsg) {
        return new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (listener == null)
                    return;

                if (task.isSuccessful()) {
                    listener.getResult(task);
                } else {
                    listener.getError(task.getException(), errorMsg);
                }
            }
        };
    }
}
