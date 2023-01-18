package org.help.hemah.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RsaKeyProperties.class, FirebaseProperties.class})
public class Config {

}
