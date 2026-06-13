package com.roomiesplit.notification.scheduler;

import com.roomiesplit.expense.entity.ExpenseParticipant;
import com.roomiesplit.expense.entity.ExpenseStatus;
import com.roomiesplit.expense.repository.ExpenseParticipantRepository;
import com.roomiesplit.notification.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final ExpenseParticipantRepository expenseParticipantRepository;
    private final NotificationService notificationService;

    @PostConstruct
    public void init() {
        System.out.println("ReminderScheduler Loaded");
    }

    @Transactional
    @Scheduled(cron = "*/30 * * * * *")   // every 30 seconds
    // @Scheduled(cron = "0 0 9 * * *")   // triggers at morning 9 AM
    public void sendPaymentReminders() {

        log.info("Running reminder scheduler...");

        var pendingParticipants =
                expenseParticipantRepository
                        .findByStatus(
                                ExpenseStatus.PENDING
                        );

        for (ExpenseParticipant participant
                : pendingParticipants) {

            notificationService.createNotification(

                    participant.getUser(),

                    "Payment Reminder",

                    "You still owe ₹"
                            + participant.getShareAmount()
                            + " for "
                            + participant.getExpense()
                            .getTitle()
            );
        }

        log.info(
                "Created {} reminder notifications",
                pendingParticipants.size()
        );
    }
}
