package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.telegrambot.keyboard.KeyboardFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BackHandler implements BotTextHandler{

    private final KeyboardFactory keyboardFactory;
    private final MessageSender messageSender;

    @Override
    public boolean canHandle(String text) {
        return "Назад".equals(text);
    }

    @Override

    public void handle(long chatId, String text) {
        messageSender.send(
                keyboardFactory.createMainMenu(chatId, "Главное меню")

        );
    }
}
