package com.userauth.userauth.firestore;

import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirebaseConfig {
    @Bean
    public Firestore getDb() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("path/to/service_account_pk.json");

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);   
        FirebaseOptions options = FirebaseOptions
            .builder()
            .setCredentials(credentials)
            .setStorageBucket("e-com-services-f8918.appspot.com")
            .build();
        return FirestoreClient.getFirestore();
    }
}
