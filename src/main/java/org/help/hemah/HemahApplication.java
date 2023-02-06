package org.help.hemah;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

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
