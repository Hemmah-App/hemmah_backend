package org.help.hemah.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.model.Disabled;
import org.help.hemah.service.TokenService;
import org.help.hemah.service.disabled.DisabledService;
import org.help.hemah.service.help_video.HelpVideoService;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HelpVideoController {

    private final TokenService tokenService;

    private final DisabledService disabledService;
    private final HelpVideoService helpVideoService;

    private final SimpMessagingTemplate template;

    @MessageMapping("/notify_all")
    @SendTo("/all")
    public String sendTest(final String message) {

        return message;
    }


    // write method to send help_call request to all the available volunteers
    @MessageMapping("/help_call/send")
    public void sendHelpCall(@AuthenticationPrincipal Jwt jwt) {
        helpVideoService.sendHelpCall((Disabled) tokenService.getUser(jwt));
    }

    @MessageMapping("/help_call/accept")
    public void volAcceptedCall(final String roomUrl) {
        helpVideoService.acceptCall(roomUrl);
    }

}
