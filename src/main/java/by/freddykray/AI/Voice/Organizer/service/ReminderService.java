package by.freddykray.AI.Voice.Organizer.service;

import by.freddykray.AI.Voice.Organizer.model.reminder.RequestReminder;
import by.freddykray.AI.Voice.Organizer.repository.ReminderRepository;
import by.freddykray.AI.Voice.Organizer.service.userdialogstate.UserDialogStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserDialogStateService userDialogService;

    public String create(RequestReminder request) {
        if (request.getDeadline() == null) {
            userDialogService.saveTempRemindHour(request.getTitle(), request.getChatId());
            return "ASK_REMINDER_TIME";
        }

        reminderRepository.create(request);
        if (request.getDescription() != null) {
            return "Напоминание: " + request.getTitle() + " создано.\n" +
                    "Описание: " + request.getDescription() +
                    "Время напоминания: " + request.getDeadline();
        } else {
            return "Напоминание: " + request.getTitle() + " создано.\n" +
                    "Время напоминания: " + request.getDeadline();
        }
    }
}
