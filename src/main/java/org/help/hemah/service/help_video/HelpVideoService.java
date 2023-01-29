package org.help.hemah.service.help_video;

import org.help.hemah.model.Disabled;

public interface HelpVideoService {

    void sendHelpCall(Disabled disabled);

    String generateHelpRoomUrl(Disabled disabled);


    void acceptCall(String roomUrl);
}
