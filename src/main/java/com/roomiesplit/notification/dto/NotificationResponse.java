package com.roomiesplit.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponse(

        Long id,

        String title,

        String message,

        Boolean isRead,

        LocalDateTime createdAt

) {
}