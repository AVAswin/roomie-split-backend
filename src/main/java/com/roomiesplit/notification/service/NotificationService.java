package com.roomiesplit.notification.service;

import com.roomiesplit.notification.dto.NotificationResponse;
import com.roomiesplit.notification.entity.Notification;
import com.roomiesplit.notification.repository.NotificationRepository;
import com.roomiesplit.user.entity.User;
import com.roomiesplit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void createNotification(
            User user,
            String title,
            String message
    ) {

        Notification notification =
                Notification.builder()
                        .user(user)
                        .title(title)
                        .message(message)
                        .isRead(false)
                        .createdAt(LocalDateTime.now())
                        .build();

        notificationRepository.save(
                notification
        );
    }

    public List<NotificationResponse> getNotifications(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(notification ->
                        new NotificationResponse(
                                notification.getId(),
                                notification.getTitle(),
                                notification.getMessage(),
                                notification.getIsRead(),
                                notification.getCreatedAt()
                        )
                )
                .toList();
    }

    @Transactional
    public void markAsRead(
            Long notificationId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        Notification notification =
                notificationRepository
                        .findByIdAndUser(
                                notificationId,
                                user
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Notification not found"
                                )
                        );

        notification.setIsRead(true);

        notificationRepository.save(
                notification
        );
    }

    public Long getUnreadCount(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        return notificationRepository
                .countByUserAndIsReadFalse(
                        user
                );
    }
}
