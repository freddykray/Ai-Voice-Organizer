package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
        void send(long chatId, String text);
        void send(SendMessage message);


}