package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class TelegramMessageSender implements MessageSender {

    private final TelegramClient telegramClient;

    @Override
    public void send(long chatId, String text) {
        try {
            telegramClient.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void answerCallback(String callbackQueryId) {
        try {
            telegramClient.execute(new AnswerCallbackQuery(callbackQueryId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
