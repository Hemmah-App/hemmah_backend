package org.help.hemah.service.help_video;

import org.help.hemah.model.disabled.Disabled;
import org.help.hemah.model.volunteer.Volunteer;

public interface HelpVideoCallService {

    void requestHelpCall(Disabled disabled);

    void acceptCall(Volunteer volunteer, String roomName);
}
