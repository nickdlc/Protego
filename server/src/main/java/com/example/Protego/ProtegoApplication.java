package com.example.Protego;

import com.example.Protego.FirebaseAttributes.FirebaseAttributes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ProtegoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProtegoApplication.class, args);

		// Start Firebase application SDK
		try {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.getApplicationDefault())
					.build();

			FirebaseAttributes.firebaseApp = FirebaseApp.initializeApp(options);

			FirebaseAttributes.firestore = FirestoreClient.getFirestore();

			FirebaseAttributes.firebaseAuth = FirebaseAuth.getInstance();
		} catch (IOException e) {
			System.out.println("Could not create Firebase options, exiting...");
			e.printStackTrace();
			System.exit(1);
		}

	}

}
