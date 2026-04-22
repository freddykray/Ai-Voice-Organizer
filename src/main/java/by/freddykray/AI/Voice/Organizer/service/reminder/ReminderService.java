package by.freddykray.AI.Voice.Organizer.service.reminder;

import by.freddykray.AI.Voice.Organizer.model.reminder.RequestReminder;
import by.freddykray.AI.Voice.Organizer.model.reminder.ResponseReminder;
import by.freddykray.AI.Voice.Organizer.repository.ReminderRepository;
import by.freddykray.AI.Voice.Organizer.service.DateTextFormatter;
import by.freddykray.AI.Voice.Organizer.service.userdialogstate.UserDialogStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserDialogStateService userDialogService;
    private final DateTextFormatter timeFormatter;


    public String create(RequestReminder request) {
        if (request.getDeadline() == null) {
            userDialogService.saveTempRemindHour(request.getTitle(), request.getChatId());
            return "ASK_REMINDER_TIME";
        }

        reminderRepository.create(request);

        if (request.getDescription() != null) {
            return "Напоминание: " + request.getTitle() + " создано.\n" +
                    "Описание: " + request.getDescription() +
                    "Время напоминания: " + timeFormatter.formatReminder(request.getDeadline(), request.isHasExactTime());
        } else {
            return "Напоминание: " + request.getTitle() + " создано.\n" +
                    "Время напоминания: " + timeFormatter.formatReminder(request.getDeadline(), request.isHasExactTime());
        }
    }

    public void deleteReminder(long reminderId) {
        reminderRepository.deleteReminder(reminderId);
    }

   public List<ResponseReminder> getAllReminderUser(long chatId) {
        return reminderRepository.getAllReminderUser(chatId);
   }
}
