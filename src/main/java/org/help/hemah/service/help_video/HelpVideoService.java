package org.help.hemah.service.help_video;

import org.help.hemah.model.Disabled;
import org.help.hemah.model.Volunteer;

public interface HelpVideoService {

    void sendHelpCall(Disabled disabled);

    void acceptCall(Volunteer volunteer, String roomName);
}
