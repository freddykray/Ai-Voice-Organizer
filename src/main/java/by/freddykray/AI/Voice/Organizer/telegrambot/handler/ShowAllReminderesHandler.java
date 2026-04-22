package by.freddykray.AI.Voice.Organizer.telegrambot.handler;


import by.freddykray.AI.Voice.Organizer.model.reminder.ResponseReminder;
import by.freddykray.AI.Voice.Organizer.model.task.ResponseTask;
import by.freddykray.AI.Voice.Organizer.service.DateTextFormatter;
import by.freddykray.AI.Voice.Organizer.service.reminder.ReminderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@AllArgsConstructor
public class ShowAllReminderesHandler implements BotTextHandler{

    private final DateTextFormatter dateTextFormatter;
    private final MessageSender messageSender;
    private final ReminderService reminderService;

    @Override
    public boolean canHandle(String text) {
        return "Мои напоминания".equals(text);
    }

    @Override
    public void handle(long chatId, String text) {
        List<ResponseReminder> reminders = reminderService.getAllReminderUser(chatId);

        if (reminders.isEmpty()) {
            messageSender.send(chatId, "У вас пока нет напоминаний.");
            return;
        }
        for (ResponseReminder reminder : reminders) {

            String taskText = buildTaskText(reminder);
            SendMessage message = new SendMessage(String.valueOf(chatId), taskText);
            message.setReplyMarkup(buildReminderKeyboard(reminder.getId()));
            messageSender.send(message);
        }
    }

    private String buildTaskText(ResponseReminder task) {
        String deadlineText = dateTextFormatter.formatReminder(
                task.getDeadline(),
                task.isHasExactTime()
        );

        return """
                Напоминание: %s
                Время напоминания: %s
                """.formatted(task.getTitle(), deadlineText);
    }

    private InlineKeyboardMarkup buildReminderKeyboard(long reminderId) {
        InlineKeyboardButton doneButton = new InlineKeyboardButton("❌Удалить"); //todo изменить время напоминания
        doneButton.setCallbackData("delete_reminder:" + reminderId);
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(doneButton);
        return new InlineKeyboardMarkup(List.of(row));
    }
}
