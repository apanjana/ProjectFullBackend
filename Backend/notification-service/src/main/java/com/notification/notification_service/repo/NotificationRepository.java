package com.notification.notification_service.repo;

import com.notification.notification_service.model.Notification;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification,Long> {

    List<Notification> findByBusId(Long busId);
    List<Notification> findByUserRole(String userRole);

}
