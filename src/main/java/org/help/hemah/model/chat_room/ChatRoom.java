package org.help.hemah.model.chat_room;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.help.hemah.model.BaseEntity;
import org.help.hemah.model.disabled.Disabled;
import org.help.hemah.model.help_request.HelpRequest;
import org.help.hemah.model.volunteer.Volunteer;

@Entity(name = "chat_room")
@Data
public class ChatRoom extends BaseEntity {

    @OneToOne
    private Disabled disabled;
    @OneToOne
    private Volunteer volunteer;
    @OneToOne
    private HelpRequest helpRequest;

}
