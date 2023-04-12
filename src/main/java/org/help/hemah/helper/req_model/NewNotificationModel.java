package org.help.hemah.helper.req_model;

import lombok.Data;
import org.help.hemah.domain.notification.NotificationType;
import org.hibernate.validator.constraints.Length;

@Data
public class NewNotificationModel {
    @Length(max = 150)
    private String title;
    @Length(max = 255)
    private String body;
    private NotificationType type;
}
