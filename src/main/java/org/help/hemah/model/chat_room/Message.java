package org.help.hemah.model.chat_room;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.help.hemah.model.BaseEntity;
import org.help.hemah.model.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;
    private String content;
    @CreationTimestamp
    private Date createdAt;
}
