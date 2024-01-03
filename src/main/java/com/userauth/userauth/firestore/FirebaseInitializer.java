package com.userauth.userauth.firestore;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseInitializer {
    
    @PostConstruct
    public void initialize() throws IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        // FileInputStream serviceAccount = new FileInputStream("./service_account_pk.json");
        FileInputStream serviceAccount;

        if (credentialsPath != null) {
            serviceAccount = new FileInputStream(credentialsPath);
        } else {
            serviceAccount = new FileInputStream("src/main/resources/service_account_pk.json");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);   
        FirebaseOptions options = FirebaseOptions
            .builder()
            .setCredentials(credentials)
            .setStorageBucket("e-com-services-f8918.appspot.com")
            .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
