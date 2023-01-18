package org.help.hemah.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class HelpVideoController {

    SimpMessagingTemplate template;

    public HelpVideoController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/notify_all")
    @SendTo("/all")
    public String sendTest(final String message) {

        return message;
    }

    // write method to send help_call request to all the available volunteers
    @MessageMapping("/help_call/send")
    public void sendHelpCall(final String message) {
        template.convertAndSend("/all", message);
    }


    @MessageMapping("/help_call/accept")
    public void volAcceptedCall(final String roomUrl) {

    }


}
