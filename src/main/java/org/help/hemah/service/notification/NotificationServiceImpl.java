package org.help.hemah.service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.domain.notification.Notification;
import org.help.hemah.helper.req_model.NewNotificationModel;
import org.help.hemah.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseMessaging fcm;
    private final SimpMessagingTemplate sMTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotificationToAll(NewNotificationModel model) throws FirebaseMessagingException {
        Notification notification = new Notification(model.getTitle(), model.getBody(), model.getType());
        notificationRepository.save(notification);
        sMTemplate.convertAndSend("/notification", notification);


        Message notificationM = Message.builder()
                .putData("title", model.getTitle())
                .putData("body", model.getBody())
                .putData("type", model.getType().toString())
                .setTopic("notification")
                .build();

        fcm.send(notificationM);
    }
}
