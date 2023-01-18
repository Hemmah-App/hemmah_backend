package org.help.hemah.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "firebase")
public record FirebaseProperties(String databaseUrl, String serviceAccountFile) {

}

