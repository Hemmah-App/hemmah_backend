package org.help.hemah.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    GoogleCredentials googleCredentials(FirebaseProperties firebaseProperties) throws Exception {
        File file = resourceLoader.getResource(firebaseProperties.serviceAccountFile()).getFile();

        log.info(FileUtils.readFileToString(file));

        return GoogleCredentials.fromStream(new FileInputStream(file));
    }
}
