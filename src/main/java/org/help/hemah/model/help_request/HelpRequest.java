package org.help.hemah.model.help_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.model.BaseEntity;
import org.help.hemah.model.disabled.Disabled;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class HelpRequest extends BaseEntity {

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String title;
    @Column(columnDefinition = "VARCHAR(5000)")
    private String description;
    private LocalDate date;
    @JsonIgnore
    @ManyToOne
    private Disabled disabled;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // Location
    private String meetingLocation;
    private Double longitude;
    private Double latitude;

    public HelpRequest(String title, String description, LocalDate date, Disabled disabled, RequestStatus status, String meetingLocation) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.disabled = disabled;
        this.status = status;
        this.meetingLocation = meetingLocation;
    }
}
