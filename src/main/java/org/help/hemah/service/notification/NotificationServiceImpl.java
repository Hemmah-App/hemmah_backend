package org.help.hemah.service.notification;

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
    private final SimpMessagingTemplate sMTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotificationToAll(NewNotificationModel model) {
        Notification notification = new Notification(model.getTitle(), model.getBody(), model.getType());
        notificationRepository.save(notification);
        sMTemplate.convertAndSend("/notification", notification);
    }
}
