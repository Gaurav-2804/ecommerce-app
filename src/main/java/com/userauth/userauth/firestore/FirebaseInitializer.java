// package com.userauth.userauth.firestore;

// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;

// import javax.annotation.PostConstruct;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.ResourceLoader;
// import org.springframework.stereotype.Service;

// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;

// @Service
// public class FirebaseInitializer {
//     @Autowired
//     ResourceLoader resourceLoader;
    
//     @PostConstruct
//     public void initialize() throws IOException {
//         // String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

//         Resource resource = resourceLoader.getResource("classpath:service_account_pk.json");

//         // FileInputStream serviceAccount = new FileInputStream("./service_account_pk.json");
//         // FileInputStream serviceAccount;

//         InputStream inputStream = resource.getInputStream();

//         // if (credentialsPath != null) {
//         //     serviceAccount = new FileInputStream(credentialsPath);
//         // } else {
//         //     serviceAccount = new FileInputStream("./service_account_pk.json");
//         // }

//         GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);   
//         FirebaseOptions options = FirebaseOptions
//             .builder()
//             .setCredentials(credentials)
//             .setStorageBucket("e-com-services-f8918.appspot.com")
//             .build();

//         if (FirebaseApp.getApps().isEmpty()) {
//             FirebaseApp.initializeApp(options);
//         }
//     }
// }
