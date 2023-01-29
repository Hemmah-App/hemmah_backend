package org.help.hemah.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.messaging.access.expression.DefaultMessageSecurityExpressionHandler;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;

@Configuration
@EnableConfigurationProperties({RsaKeyProperties.class, FirebaseProperties.class})
public class Config {

}
