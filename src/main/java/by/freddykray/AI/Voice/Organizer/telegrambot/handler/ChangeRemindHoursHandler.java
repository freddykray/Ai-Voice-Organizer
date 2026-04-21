package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.service.userdialogstate.UserDialogStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChangeRemindHoursHandler implements BotTextHandler {

    private final MessageSender messageSender ;
    private final UserDialogStateService userDialogStateService;

    @Override
    public boolean canHandle(String text) {
        return "Настроить предупреждение о сроке. ⏰".equals(text);
    }

    @Override
    public void handle(long chatId, String text) {
        messageSender.send(chatId, "Напиши, за сколько часов до дедлайна напоминать");
        userDialogStateService.saveTempRemindHour(chatId);
    }
}
