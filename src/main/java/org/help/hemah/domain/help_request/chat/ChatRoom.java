package org.help.hemah.domain.help_request.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.domain.BaseEntity;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.help_request.HelpRequest;
import org.help.hemah.domain.volunteer.Volunteer;

import java.util.List;

@Entity(name = "chat_room")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"disabled", "volunteer", "messages"})
public class ChatRoom extends BaseEntity {

    @OneToOne
    private Disabled disabled;
    @OneToOne
    private Volunteer volunteer;

    @ManyToOne
    @JoinColumn(name = "help_request_id")
    private HelpRequest helpRequest;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<Message> messages;

    public ChatRoom(Disabled disabled, Volunteer volunteer, HelpRequest helpRequest) {
        this.disabled = disabled;
        this.volunteer = volunteer;
        this.helpRequest = helpRequest;
    }


    public boolean isParty(String username) {
        return this.volunteer.getUserData().getUsername().equals(username) || this.disabled.getUserData().getUsername().equals(username);
    }
}
