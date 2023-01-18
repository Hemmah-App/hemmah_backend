package org.help.hemah.service.help_video;

import org.help.hemah.model.Disabled;
import org.help.hemah.model.User;
import org.help.hemah.model.Volunteer;
import org.help.hemah.service.user.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HelpVideoServiceImpl implements HelpVideoService {

    private static final String jitsiUrl = "https://meet.jit.si/";
    private static final String videoConfigUrl = "#config.prejoinPageEnabled=false&config.disableDeepLinking=true&config.startWithVideoMuted=false&config.startWithAudioMuted=false";

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    public HelpVideoServiceImpl(SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @Override
    public String generateHelpRoomUrl(Disabled disabled) {
        User user = userService.getUserByUsername(disabled.getUserData().getUsername());
        return jitsiUrl + disabled.getUserData().getUsername() + UUID.randomUUID() + videoConfigUrl;
    }

    public void callVolunteer(String roomUrl) {
        List<Volunteer> volunteers = userService.getActiveVolunteers();

        int randomVolunteerIndex = (int) (Math.random() * volunteers.size());
        volunteers.get(randomVolunteerIndex);
        volunteers.forEach(volunteer -> {
            messagingTemplate.convertAndSendToUser(volunteer.getUserData().getUsername(), "/help_call", roomUrl);
        });

    }

    public void acceptCall(String roomUrl) {
        messagingTemplate.convertAndSendToUser("disabled", "/help_call/accept", roomUrl);
    }
}
