package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.telegrambot.keyboard.KeyboardFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SettingsMenuHandler implements BotTextHandler {

    private final MessageSender messageSender;
    private final KeyboardFactory keyboardFactory;

    @Override
    public boolean canHandle(String text) {
        return "Настройки".equals(text);
    }

    @Override
    public void handle(long chatId, String text) {
        messageSender.send(
                keyboardFactory.createSettingMenu(chatId)

        );
    }
}