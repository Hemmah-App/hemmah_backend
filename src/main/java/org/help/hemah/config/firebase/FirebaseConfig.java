package org.help.hemah.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;

@Configuration
@Slf4j
public class FirebaseConfig {


    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    GoogleCredentials googleCredentials(FirebaseProperties firebaseProperties) throws Exception {
        File file = new File(firebaseProperties.serviceAccountFile());

        log.info(FileUtils.readFileToString(file));

        return GoogleCredentials.fromStream(new FileInputStream(file));
    }
}
