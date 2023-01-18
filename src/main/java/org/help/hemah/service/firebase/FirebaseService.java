package org.help.hemah.service.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.config.FirebaseProperties;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Service
public class FirebaseService {

    public FirebaseService(FirebaseProperties firebaseProperties) throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(firebaseProperties.serviceAccountFile());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        log.info("Firebase Initialized");
    }

}
