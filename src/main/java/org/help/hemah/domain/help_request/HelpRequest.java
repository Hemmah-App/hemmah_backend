package org.help.hemah.domain.help_request;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.domain.BaseEntity;
import org.help.hemah.domain.disabled.Disabled;
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


    public void setDate(LocalDate date) {
        if (date != null) {
            this.date = date;
        }
    }

    public void setStatus(RequestStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void setTitle(String title) {
        if (!(title == null) && !title.isBlank()) {
            this.title = title;
        }
    }

    public void setDescription(String description) {
        if (!(description == null) && !description.isBlank()) {
            this.description = description;
        }
    }


    public void setMeetingLocation(String meetingLocation) {
        if (!(meetingLocation == null) && !meetingLocation.isBlank()) {
            this.meetingLocation = meetingLocation;
        }
    }

    public void setLongitude(Double longitude) {
        if (!(longitude == null) && !longitude.isNaN() && longitude != 0.0) {
            this.longitude = longitude;
        }
    }

    public void setLatitude(Double latitude) {
        if (!(latitude == null) && !latitude.isNaN() && latitude != 0.0) {
            this.latitude = latitude;
        }
    }
}
