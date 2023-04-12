package org.help.hemah.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.volunteer.Volunteer;
import org.help.hemah.helper.req_model.RoomDetailsModel;
import org.help.hemah.service.help_video.HelpVideoCallService;
import org.help.hemah.service.token.TokenService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HelpVideoController {

    private final TokenService tokenService;
    private final HelpVideoCallService helpVideoCallService;

    @MessageMapping("/notify_all")
    @SendTo("/all")
    public String sendTest(final String message) {

        return message;
    }


    // For Disabled to request for help
//    @PreAuthorize("hasRole('DISABLED')")
    @MessageMapping("/help_call/ask")
    public void sendHelpCall(@AuthenticationPrincipal Jwt jwt) {
        helpVideoCallService.requestHelpCall((Disabled) tokenService.getUser(jwt));
    }

    // For Volunteer to accept the call
//    @PreAuthorize("hasRole('VOLUNTEER')")
    @MessageMapping("/help_call/answer")
    public void volAcceptsCall(@AuthenticationPrincipal Jwt jwt, @RequestBody RoomDetailsModel roomDetailsModel) {
        helpVideoCallService.acceptCall((Volunteer) tokenService.getUser(jwt), roomDetailsModel.getRoomName());
    }

}