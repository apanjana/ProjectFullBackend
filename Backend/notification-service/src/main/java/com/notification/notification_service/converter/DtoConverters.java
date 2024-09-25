package com.notification.notification_service.converter;

import com.notification.notification_service.dto.NotificationDto;
import com.notification.notification_service.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class DtoConverters {

    public Notification toNotif(NotificationDto notificationDto){
        return new Notification(
                notificationDto.id(),
                notificationDto.busId(),
                notificationDto.message(),
                notificationDto.timestamp(),
                notificationDto.userRole()
        );
    }

    public NotificationDto toNotifDto(Notification notification){
        return new NotificationDto(
                notification.getId(),
                notification.getBusId(),
                notification.getMessage(),
                notification.getTimestamp(),
                notification.getUserRole()
        );
    }

}
