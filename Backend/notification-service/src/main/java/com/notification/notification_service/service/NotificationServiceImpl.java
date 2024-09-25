package com.notification.notification_service.service;

import com.notification.notification_service.model.Notification;
import com.notification.notification_service.repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    public final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Long busId, String message, String userRole) {
        Notification notification = new Notification();

        notification.setBusId(busId);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setUserRole(userRole);

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifByBusId(Long busId) {
        return List.copyOf(notificationRepository.findByBusId(busId));
    }

    @Override
    public List<Notification> getNotifByUserRole(String userRole) {
        return List.copyOf(notificationRepository.findByUserRole(userRole));
    }
}
