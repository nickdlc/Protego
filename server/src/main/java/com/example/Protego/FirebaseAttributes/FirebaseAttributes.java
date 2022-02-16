package com.example.Protego.FirebaseAttributes;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.stereotype.Component;

@Component
public class FirebaseAttributes {
    public static FirebaseApp firebaseApp;

    public static Firestore firestore;

    public static FirebaseAuth firebaseAuth;
}
