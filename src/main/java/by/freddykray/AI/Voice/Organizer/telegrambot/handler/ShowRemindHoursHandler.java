package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.service.TelegramUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ShowRemindHoursHandler implements BotTextHandler{

    private final MessageSender messageSender;
    private final TelegramUserService telegramUserService;

    @Override
    public boolean canHandle(String text) {
        return "За сколько ты меня предупредишь?".equals(text);
    }

    @Override
    public void handle(long chatId, String text) {
        Integer hours = telegramUserService.getHoursRemind(chatId);
        String hoursRemind = "Я предупрежу вас за: " + hours + " часов.";
        messageSender.send(chatId, hoursRemind);

    }
}
