package org.help.hemah.service.help_video;

import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.volunteer.Volunteer;

public interface HelpVideoCallService {

    void requestHelpCall(Disabled disabled);

    void acceptCall(Volunteer volunteer, String roomName);
}
