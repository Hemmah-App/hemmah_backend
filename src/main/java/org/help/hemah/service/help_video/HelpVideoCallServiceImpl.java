package org.help.hemah.service.help_video;

import com.twilio.rest.video.v1.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.model.disabled.Disabled;
import org.help.hemah.model.help_video_call.HelpVideoCall;
import org.help.hemah.model.user.UserLanguage;
import org.help.hemah.model.volunteer.Volunteer;
import org.help.hemah.repository.HelpVideoCallRepository;
import org.help.hemah.service.twilio.TwilioService;
import org.help.hemah.service.user.UserService;
import org.help.hemah.service.volunteer.VolunteerService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpVideoCallServiceImpl implements HelpVideoCallService {

    private final SimpMessagingTemplate messagingTemplate;
    private final VolunteerService volunteerService;
    private final UserService userService;
    private final HelpVideoCallRepository helpVideoCallRepository;
    private final TwilioService twilioService;

    @Override
    public void requestHelpCall(Disabled disabled) {
        log.info(disabled.getUserData().getEmail() + " is requesting for help");

        UserLanguage disLang = userService.getUser(disabled.getUserData().getUsername()).getLanguage();

        // Get active volunteers with the same language as the disabled
        List<Volunteer> volunteers = volunteerService.getActiveVolunteers()
                .stream()
                .filter(vol -> userService.getUser(vol.getUserData().getUsername()).getLanguage().equals(disLang))
                .toList();

        if (volunteers.isEmpty()) {
            log.debug("No volunteers available");
            messagingTemplate.convertAndSendToUser(disabled.getUserData().getUsername(), "/help_call/ask", Map.of("message", "No volunteers available"));
            return;
        }

        String roomName = disabled.getUserData().getFirstName() + " " + UUID.randomUUID();
        twilioService.createVideoRoom(roomName, Room.RoomType.GO);
        String roomToken = twilioService.generateVideoToken(disabled.getUserData().getFirstName(), roomName);

        // Save help video call
        HelpVideoCall helpVideoCall = new HelpVideoCall(roomName, disabled);
        helpVideoCallRepository.save(helpVideoCall);

        messagingTemplate.convertAndSendToUser(
                disabled.getUserData().getUsername(),
                "/help_call/ask",
                Map.of("roomName", roomName, "roomToken", roomToken));

        callVolunteers(roomName, volunteers);

    }

    public void callVolunteers(String roomName, List<Volunteer> volunteers) {

        volunteers.forEach(volunteer -> {
            String roomToken = twilioService.generateVideoToken(volunteer.getUserData().getFirstName(), roomName);
            messagingTemplate.convertAndSendToUser(volunteer.getUserData().getUsername(), "/help_call/answer", Map.of("roomName", roomName, "roomToken", roomToken));
            log.info("Calling volunteer " + volunteer.getUserData().getUsername());
        });
    }

    @Override
    public void acceptCall(Volunteer volunteer, String roomName) {
        // Update help video call
        HelpVideoCall helpVideoCall = helpVideoCallRepository.findByRoomName(roomName).orElseThrow();
        helpVideoCall.setVolunteer(volunteer);
        helpVideoCall.setAcceptedAt(LocalDateTime.now());
        helpVideoCallRepository.save(helpVideoCall);

        // Update room status
        twilioService.updateVideoRoomStatus(roomName, Room.RoomStatus.COMPLETED);

    }

}
