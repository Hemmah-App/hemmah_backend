package org.help.hemah;

import lombok.extern.slf4j.Slf4j;
import org.help.hemah.model.User;
import org.help.hemah.repository.UserRepository;
import org.help.hemah.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class HemahApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(HemahApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
        };
    }

}
