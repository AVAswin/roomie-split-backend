package com.roomiesplit.notification.repository;

import com.roomiesplit.notification.entity.Notification;
import com.roomiesplit.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByUserOrderByCreatedAtDesc(
            User user
    );

    Optional<Notification> findByIdAndUser(
            Long id,
            User user
    );

    long countByUserAndIsReadFalse(
            User user
    );
}