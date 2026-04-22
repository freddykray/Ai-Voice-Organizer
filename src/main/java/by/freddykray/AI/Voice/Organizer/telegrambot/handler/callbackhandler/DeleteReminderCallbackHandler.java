package by.freddykray.AI.Voice.Organizer.telegrambot.handler.callbackhandler;

import by.freddykray.AI.Voice.Organizer.service.reminder.ReminderService;
import by.freddykray.AI.Voice.Organizer.telegrambot.handler.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteReminderCallbackHandler implements BotCallbackHandler{

    private final MessageSender messageSender;
    private final ReminderService reminderService;


    @Override
    public boolean canHandle(String callbackData) {
        return callbackData != null && callbackData.startsWith("delete_reminder:");
    }

    @Override
    public void handle(long chatId, String callbackData) {
        long reminderId = Long.parseLong(callbackData.substring("delete_reminder:".length()));
        reminderService.deleteReminder(reminderId);
        messageSender.send(chatId, "Напоминание успешно удалено!");
    }
}
