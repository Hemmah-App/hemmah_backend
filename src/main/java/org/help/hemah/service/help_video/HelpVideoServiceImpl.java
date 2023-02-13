package org.help.hemah.service.help_video;

import com.twilio.rest.video.v1.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.model.Disabled;
import org.help.hemah.model.Volunteer;
import org.help.hemah.service.disabled.DisabledService;
import org.help.hemah.service.twilio.TwilioService;
import org.help.hemah.service.user.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpVideoServiceImpl implements HelpVideoService {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserService userService;

    private final DisabledService disabledService;

    private final TwilioService twilioService;

    @Override
    public void requestHelpCall(Disabled disabled) {
        log.info(disabled.getUserData().getEmail() + " is requesting for help");
        List<Volunteer> volunteers = userService.getActiveVolunteers();
        if (volunteers.isEmpty()) {
            log.debug("No volunteers available");
            messagingTemplate.convertAndSendToUser(disabled.getUserData().getUsername(), "/help_call/ask", Map.of("message", "No volunteers available"));

        } else {
            String roomName = UUID.randomUUID() + disabled.getUserData().getFirstName();
            twilioService.createVideoRoom(roomName, Room.RoomType.GO);
            String disabledToken = twilioService.generateVideoToken(disabled.getUserData().getFirstName(), roomName);
            messagingTemplate.convertAndSendToUser(disabled.getUserData().getUsername(), "/help_call/ask", Map.of("roomName", roomName, "roomToken", disabledToken));
            callVolunteer(roomName);
        }
    }

    public void callVolunteer(String roomName) {
        List<Volunteer> volunteers = userService.getActiveVolunteers();

        volunteers.forEach(volunteer -> {
            String roomToken = twilioService.generateVideoToken(volunteer.getUserData().getFirstName(), roomName);
            messagingTemplate.convertAndSendToUser(volunteer.getUserData().getUsername(), "/help_call/answer", Map.of("roomName", roomName, "roomToken", roomToken));
            log.info("Calling volunteer " + volunteer.getUserData().getUsername());
        });
    }

    @Override
    public void acceptCall(Volunteer volunteer, String roomName) {
        twilioService.updateVideoRoomStatus(roomName, Room.RoomStatus.COMPLETED);
    }
}
