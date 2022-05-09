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
import com.example.protego.web.schemas.Notification;
import com.example.protego.web.schemas.NotificationType;
import com.example.protego.web.schemas.Patient;
import com.example.protego.web.schemas.Vital;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;


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

    public void updateUser(FirebaseUser user,
                           Map<String, Object> data,
                           FirestoreListener<Task> listener) {
        db.collection("users")
                .document(user.getUid())
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successfully updated user " + user.getUid());
                        } else {
                            Log.e(TAG, "Unable to update user " + user.getUid());
                        }
                    }
                });
    }

    public Task<DocumentReference> createNote(String puid,
                                              List<String> approvedDoctors,
                                              String title,
                                              String content,
                                              String visibility,
                                              FirestoreListener<Task> listener) {
        Note note = new Note();
        note.setCreator(puid);
        note.setTitle(title);
        Date date = new Date();
        note.setDateCreated(date);
        note.setContent(content);
        note.setVisibility(visibility);
        note.setApprovedDoctors(approvedDoctors);



        return db.collection("users")
                .document(puid).collection("Notes")
                .add(note)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to create note..."));
    }


    public Task<Void> deleteNote(String puid, String note_id, FirestoreListener<Task> listener) {


         Task<Void> task = db.collection("users")
                .document(puid).collection("Notes")
                .document(note_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error in deleting the document", e);
                    }
                });

        return task;
    }

    public Task<QuerySnapshot> queryNote(String puid, String note_id, FirestoreListener<Task> listener) {



        CollectionReference collection = db.collection("users")
                .document(puid).collection("Notes");
        Query query = collection.whereEqualTo("noteID", note_id).limit(1);

        Task task_result = query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.v(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.v(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return task_result;

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
        String date = getCurrentFormattedDate();

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
                                                    List<String> approvedDoctors,
                                                    String name,
                                                    String dosage,
                                                    String prescriber,
                                                    String frequency,
                                                    FirestoreListener<Task> listener) {
        // Convert the current date to format `yyyy-MM-dd'T'HH:mm'Z'`
        //String date = getCurrentFormattedDate();

        Medication med = new Medication();
        med.setPrescribee(prescribee);
        med.setName(name);
        Date date = new Date();
        med.setDatePrescribed(date);
        med.setDosage(dosage);
        med.setPrescriber(prescriber);
        med.setApprovedDoctors(approvedDoctors);
        med.setFrequency(frequency);

        return db.collection("users")
                .document(prescribee).collection("Medications")
                .add(med)
                .addOnCompleteListener(getListenerForCreation(listener, "Failed to create medical info..."));
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
                .whereEqualTo("active", true)
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
                                                                p.setPatientID(puid);
                                                                patients.add(p);
                                                                if (patients.size() >= totalPatients) {
                                                                    listener.getResult(patients);
                                                                }
                                                                Log.d(TAG, "got puid " + p.getPatientID());
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

    public void getPatientsDoctors(String puid,
                                   FirestoreListener<List<Doctor>> listener) {
        db.collection("AssignedTo")
                .whereEqualTo("patient", puid)
                .whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get the AssignedTo list, then get the doctors
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            int totalDoctors = documents.size();
                            ArrayList<Doctor> doctors = new ArrayList<>();
                            documents.stream()
                                    .map(qds ->
                                            qds.toObject(AssignedTo.class).getDoctor()).collect(Collectors.toList()).stream()
                                    .map(duid -> {
                                        return db.collection("users")
                                                .document(duid.toString())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Doctor d = task.getResult().toObject(Doctor.class);
                                                            d.setDoctorID(task.getResult().getId());
                                                            doctors.add(d);
                                                            if (doctors.size() >= totalDoctors) {
                                                                listener.getResult(doctors);
                                                            }
                                                            Log.d(TAG, "Got doctor " + d.toString());
                                                        } else {
                                                            Log.e(TAG, "Failed to get information for doctor", task.getException());
                                                        }
                                                    }
                                                });
                                    }).collect(Collectors.toList());
                        } else {
                            Log.e(TAG, "Failed to get AssignedTo collection for patient uid " + puid, task.getException());
                        }
                    }
                });
    }

    /**
     *
     * @param puid
     * @param duid
     * @param active
     * @param listener
     * @return
     *
     * Creates an AssignedTo object from Firestore with puid, duid, and active. The AssignedTo
     * object is null if the connection does not exist.
     */
    public Task<QuerySnapshot> getConnection(String puid,
                                             String duid,
                                             Boolean active,
                                             FirestoreListener<AssignedTo> listener) {
        return db.collection("AssignedTo")
                .whereEqualTo("patient", puid)
                .whereEqualTo("doctor", duid)
                .whereEqualTo("active", active)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            AssignedTo connection;
                            if (docs.isEmpty()) {
                                // Need a null connection when checking for existing active requests
                                // when creating a ConnectionRequest
                                connection = null;
                            } else {
                                // Get the first document; there should only be one active connection
                                // between a given patient and doctor
                                connection = docs.get(0).toObject(AssignedTo.class);
                            }
                            listener.getResult(connection);
                        } else {
                            listener.getError(task.getException(),
                                    "Failed to get AssignedTo connection with puid = " +
                                    puid + ", duid = " + duid);
                        }
                    }
                });
    }

    public Task<DocumentReference> createConnection(String puid,
                                                    String duid,
                                                    FirestoreListener<Task> listener) {
        // Create an active AssignedTo connection for the patient puid and doctor duid
        Map<String, Object> params = new HashMap<>();
        params.put("active", true);
        params.put("patient", puid);
        params.put("doctor", duid);

        return db.collection("AssignedTo")
                .add(params)
                .addOnCompleteListener(getListenerForCreation(listener,
                        "Failed to created AssignedTo connection..."));
    }

    public void cancelConnection(String puid,
                                 String duid,
                                 FirestoreListener<Task> listener) {
        db.collection("AssignedTo")
                .whereEqualTo("patient", puid)
                .whereEqualTo("doctor", duid)
                .whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference dr = db.collection("AssignedTo")
                                        .document(document.getId());

                                batch.update(dr, "active", false);

                                Log.d(TAG, "Added to batch: " + document.getId());
                            }

                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "Committed batch to cancel connection");
                                }
                            });
                            listener.getResult(task);
                        } else {
                            listener.getError(
                                    task.getException(),
                                    "Failed to cancel connection..."
                            );
                        }
                    }
                });
    }

    public Task<DocumentReference> createConnectionRequest(String puid,
                                                           String duid,
                                                           FieldValue timestamp,
                                                           FirestoreListener<Task> listener) {
        Map<String, Object> params = new HashMap<>();

        params.put("duid", duid);
        params.put("puid", puid);
        params.put("active", true);
        params.put("timeCreated", timestamp);
        return db.collection("ConnectionRequest")
                .add(params)
                .addOnCompleteListener(getListenerForCreation(listener,
                        "Failed to create ConnectionRequest..."));
    }

    public void deactivateConnectionRequest(String puid,
                                            String duid,
                                            FirestoreListener<Task> listener) {
        db.collection("ConnectionRequest")
                .whereEqualTo("puid", puid)
                .whereEqualTo("duid", duid)
                .whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference dr = db.collection("ConnectionRequest")
                                        .document(document.getId());

                                batch.update(dr, "active", false);

                                Log.d(TAG, "Added to batch: " + document.getId());
                            }

                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "Committed batch to deactivate connection requests");
                                }
                            });
                        }
                    }
                });
    }

    public Task<DocumentReference> createPrescriptionNotification(String puid,
                                                      String duid,
                                                      String drLastName,
                                                      FieldValue timestamp,
                                                      FirestoreListener<Task> listener) {
        // Generate a ConnectionRequest Notification for patient puid from doctor duid at timestamp
        Map<String, Object> params = new HashMap<>();
        String msg = "You received a new prescription from Dr. " + drLastName;
        params.put("puid", puid);
        params.put("duid", duid);
        params.put("msg", msg);
        params.put("timestamp", timestamp);
        params.put("active", true);
        params.put("type", NotificationType.PRESCRIPTION.getType());

        return db.collection("Notification")
                .add(params)
                .addOnCompleteListener(getListenerForCreation(listener,
                        "Failed to create Notification..."));
    }

    public Task<DocumentReference> createNotification(String puid,
                                                      String duid,
                                                      String drLastName,
                                                      FieldValue timestamp,
                                                      FirestoreListener<Task> listener) {
        // Generate a ConnectionRequest Notification for patient puid from doctor duid at timestamp
        Map<String, Object> params = new HashMap<>();
        String msg = "You received a connection request from Dr. " + drLastName;
        params.put("puid", puid);
        params.put("duid", duid);
        params.put("msg", msg);
        params.put("timestamp", timestamp);
        params.put("active", true);
        params.put("type", NotificationType.CONNECTIONREQUEST.getType());

        return db.collection("Notification")
                .add(params)
                .addOnCompleteListener(getListenerForCreation(listener,
                        "Failed to create Notification..."));
    }

    public void getNotifications(String puid,
                                 Boolean active,
                                 FirestoreListener<ArrayList<Notification>> listener) {
        db.collection("Notification")
                .whereEqualTo("puid", puid)
                .whereEqualTo("active", active)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Notification> notifications = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Creating Notification " + document.getData());
                                Map<String, Object> data = document.getData();
                                notifications.add(new Notification(
                                        (String) data.get("puid"),
                                        (String) data.get("duid"),
                                        (String) data.get("msg"),
                                        (Boolean) data.get("active"),
                                        ((Timestamp) data.get("timestamp")).toDate(),
                                        data.get("type").toString()
                                        //NotificationType.CONNECTIONREQUEST.getType()
                                ));
                            }

                            listener.getResult(notifications);
                        } else {
                            listener.getError(task.getException(), "Failed to get notifications for puid = " + puid);
                        }
                    }
                });
    }

    public void deactivateNotification(String puid,
                                            String duid,
                                            FirestoreListener<Task> listener) {
        db.collection("Notification")
                .whereEqualTo("puid", puid)
                .whereEqualTo("duid", duid)
                .whereEqualTo("active", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = db.batch();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference dr = db.collection("Notification")
                                        .document(document.getId());

                                batch.update(dr, "active", false);

                                Log.d(TAG, "Added to batch: " + document.getId());
                            }

                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "Committed batch to deactivate Notifications");
                                }
                            });
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




    public static String getCurrentFormattedDate() {
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
