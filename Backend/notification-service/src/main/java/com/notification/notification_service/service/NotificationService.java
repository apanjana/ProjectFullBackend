package com.notification.notification_service.service;

import com.notification.notification_service.model.Notification;

import java.util.List;

public interface NotificationService {

    public Notification createNotification(Long busId, String message, String userRole);

    public List<Notification> getNotifByBusId(Long busId);

    public List<Notification> getNotifByUserRole(String userRole);

}
