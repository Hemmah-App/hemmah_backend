package org.help.hemah.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Data
public class HelpMeeting extends BaseEntity {

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(targetEntity = Disabled.class)
    private Disabled disabled;

    @ManyToOne(targetEntity = Volunteer.class)
    private Volunteer volunteer;

    private String longitude;

    private Point location;

}
