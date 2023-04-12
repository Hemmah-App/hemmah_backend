package org.help.hemah.domain.help_video_call;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.domain.BaseEntity;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.volunteer.Volunteer;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class HelpVideoCall extends BaseEntity {
    @Column(unique = true)
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;
    @ManyToOne
    @JoinColumn(name = "disabled_id")
    private Disabled disabled;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime acceptedAt;

    public HelpVideoCall(String roomName, Disabled disabled) {
        this.roomName = roomName;
        this.disabled = disabled;
    }

}
