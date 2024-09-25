package com.notification.notification_service.controller;

import com.notification.notification_service.converter.DtoConverters;
import com.notification.notification_service.dto.NotificationDto;
import com.notification.notification_service.model.Notification;
import com.notification.notification_service.service.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    public final NotificationServiceImpl notificationService;

    @Autowired
    public final DtoConverters dtoConverters;

    @PostMapping("/post")
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDto notificationDto){
        Notification notification = dtoConverters.toNotif(notificationDto);

        return ResponseEntity.ok().body(notificationService.createNotification(notification.getBusId(), notification.getMessage(), notification.getUserRole()));
    }

    @GetMapping("/busId")
    public  ResponseEntity<List<Notification>> getNotifByBusId(Long busId){
        return ResponseEntity.ok().body(notificationService.getNotifByBusId(busId));
    }

    @GetMapping("userRole")
    public ResponseEntity<List<Notification>> getNotifByUserRole(String userRole){
        return ResponseEntity.ok().body(notificationService.getNotifByUserRole(userRole));
    }

}
