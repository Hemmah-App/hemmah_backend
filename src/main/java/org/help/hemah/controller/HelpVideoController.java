package org.help.hemah.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.helper.req_model.RoomDetails;
import org.help.hemah.model.Disabled;
import org.help.hemah.model.Volunteer;
import org.help.hemah.service.help_video.HelpVideoService;
import org.help.hemah.service.token.TokenService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HelpVideoController {

    private final TokenService tokenService;
    private final HelpVideoService helpVideoService;

    private final SimpMessagingTemplate template;

    @MessageMapping("/notify_all")
    @SendTo("/all")
    public String sendTest(final String message) {

        return message;
    }


    // For Disabled to request for help
    @MessageMapping("/help_call/ask")
    public void sendHelpCall(@AuthenticationPrincipal Jwt jwt) {
        helpVideoService.requestHelpCall((Disabled) tokenService.getUser(jwt));
    }

    // For Volunteer to accept the call
    @MessageMapping("/help_call/answer")
    public void volAcceptsCall(@AuthenticationPrincipal Jwt jwt, @RequestBody RoomDetails roomDetails) {
        helpVideoService.acceptCall((Volunteer) tokenService.getUser(jwt), roomDetails.getRoomName());
    }

}