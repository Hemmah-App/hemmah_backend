package org.help.hemah.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resources")
public record ResourcesProperties(String profilePicsPath) {
}
