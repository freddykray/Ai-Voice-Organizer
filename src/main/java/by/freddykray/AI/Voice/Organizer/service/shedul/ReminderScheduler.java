package by.freddykray.AI.Voice.Organizer.service.shedul;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ShedulService reminderService;

    @Scheduled(fixedDelay = 60000)
    public void checkReminders() {
        reminderService.sendDueReminders();
        reminderService.sendDueTask();
    }
}