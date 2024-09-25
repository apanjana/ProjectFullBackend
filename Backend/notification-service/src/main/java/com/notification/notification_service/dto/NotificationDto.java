package com.notification.notification_service.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public record NotificationDto(

        @NotEmpty(message = "Id cannot be empty")
        String id,

        @NotEmpty(message = "busId cannot be empty")
        Long busId,

        @NotEmpty(message = "message cannot be empty")
        String message,

        @NotEmpty(message = "Time cannot be empty")
        LocalDateTime timestamp,

        @NotEmpty(message = "User Role cannot be empty")
        String userRole

) {
}
