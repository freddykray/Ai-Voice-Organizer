package by.freddykray.AI.Voice.Organizer.telegrambot.keyboard;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Configuration
public class KeyboardFactory {

    public SendMessage createMainMenu(long chatId, String text) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Мои задачи");
        row1.add("Мои идеи");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Мои напоминания");
        row2.add("Настройки");

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboard(List.of(row1, row2))
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboardMarkup)
                .build();
    }

    public SendMessage createSettingMenu(long chatId) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Настроить предупреждение о сроке. ⏰");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("За сколько ты меня предупредишь?");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Назад");

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboard(List.of(row1, row2, row3))
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text("Меню настроек")
                .replyMarkup(keyboardMarkup)
                .build();
    }
}
