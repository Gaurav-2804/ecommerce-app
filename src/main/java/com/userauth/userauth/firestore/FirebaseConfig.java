package com.userauth.userauth.firestore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirebaseConfig {
    @Bean
    public Firestore getDb() throws IOException {
        initializeFirebaseApp();
        return FirestoreClient.getFirestore();
    }   

    private void initializeFirebaseApp() throws IOException {

        InputStream serviceAccountStream = getClass().getResourceAsStream("/service_account_pk.json");
        if (serviceAccountStream == null) {
            throw new FileNotFoundException("Service account JSON file not found in the classpath");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setStorageBucket("e-com-services-f8918.appspot.com")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
