package com.roomiesplit.notification.controller;

import com.roomiesplit.common.response.ApiResponse;
import com.roomiesplit.notification.dto.NotificationResponse;
import com.roomiesplit.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<NotificationResponse>>
    getNotifications(
            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "Notifications fetched successfully",
                notificationService.getNotifications(
                        authentication.getName()
                )
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ApiResponse<Void> markAsRead(@PathVariable
            Long notificationId,
            Authentication authentication) {

        notificationService.markAsRead(
                notificationId,
                authentication.getName()
        );

        return new ApiResponse<>(
                true,
                "Notification marked as read",
                null
        );
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long>
    getUnreadCount(
            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "Unread count fetched",
                notificationService.getUnreadCount(
                        authentication.getName()
                )
        );
    }
}