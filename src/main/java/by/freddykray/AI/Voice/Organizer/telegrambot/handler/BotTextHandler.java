package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

public interface BotTextHandler {
    boolean canHandle(String text);
    void handle(long chatId, String text);
}
