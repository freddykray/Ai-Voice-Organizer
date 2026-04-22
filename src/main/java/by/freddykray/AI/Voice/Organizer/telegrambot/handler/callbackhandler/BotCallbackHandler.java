package by.freddykray.AI.Voice.Organizer.telegrambot.handler.callbackhandler;

public interface BotCallbackHandler {
    boolean canHandle(String callbackData);
    void handle(long chatId, String callbackData);
}
