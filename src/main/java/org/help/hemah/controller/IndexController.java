package org.help.hemah.controller;

import org.help.hemah.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    public IndexController(SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String index(Authentication authentication) {
        String lastName = userRepository.findByUsername(authentication.getName()).get().getBaseUserDataEntity().getLastName();
        messagingTemplate.convertAndSend("/all", "Hello, " + lastName);
        return "Hello, " + lastName;
    }

}

