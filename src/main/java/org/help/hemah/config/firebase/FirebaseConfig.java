package org.help.hemah.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.repository.VolunteerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    CommandLineRunner commandLine(VolunteerRepository volunteerRepository) {
        return args -> {

            FirebaseDatabase.getInstance().getReference().child("volunteers")
                    .setValueAsync(volunteerRepository.findAll());
        };
    }


    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setDatabaseUrl("https://hemah-64186-default-rtdb.firebaseio.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    GoogleCredentials googleCredentials(FirebaseProperties firebaseProperties) throws Exception {
        InputStream file = resourceLoader.getResource(firebaseProperties.serviceAccountFile()).getInputStream();

        return GoogleCredentials.fromStream(file);
    }
}