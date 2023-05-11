package org.help.hemah.service.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.help.hemah.helper.req_model.NewNotificationModel;

public interface NotificationService {
    void sendNotificationToAll(NewNotificationModel newNotificationModel) throws FirebaseMessagingException;
}
