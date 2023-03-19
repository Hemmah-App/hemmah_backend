package org.help.hemah.service.notification;

import org.help.hemah.helper.req_model.NewNotificationModel;

public interface NotificationService {
    void sendNotificationToAll(NewNotificationModel newNotificationModel);
}
