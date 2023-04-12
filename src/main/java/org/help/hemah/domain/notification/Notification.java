package org.help.hemah.domain.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.domain.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Notification extends BaseEntity {
    private String title;
    private String body;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Notification(String title, String body, NotificationType type) {
        this.title = title;
        this.body = body;
        this.type = type;
    }
}
