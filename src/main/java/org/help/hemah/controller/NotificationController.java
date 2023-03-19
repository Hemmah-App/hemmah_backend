package org.help.hemah.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.help.hemah.helper.req_model.NewNotificationModel;
import org.help.hemah.helper.res_model.ResponseModel;
import org.help.hemah.service.notification.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseModel> sendNotification(@RequestBody @Valid NewNotificationModel model) {
        notificationService.sendNotificationToAll(model);
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Notification sent successfully")
                        .build()
        );
    }

}
