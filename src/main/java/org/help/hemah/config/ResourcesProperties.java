package org.help.hemah.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "resources")
public record ResourcesProperties(Path profilePicsPath) {
}
