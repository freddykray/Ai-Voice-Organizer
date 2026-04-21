package by.freddykray.AI.Voice.Organizer.telegrambot;

import by.freddykray.AI.Voice.Organizer.telegrambot.handler.BotTextHandler;
import by.freddykray.AI.Voice.Organizer.telegrambot.handler.DefaultMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TextMessageRouter {

    private final List<BotTextHandler> handlers;
    private final DefaultMessageHandler defaultMessageHandler;

    public void route(long chatId, String text) {
        for (BotTextHandler handler : handlers) {
            if (handler.canHandle(text)) {
                handler.handle(chatId, text);
                return;
            }
        }

        defaultMessageHandler.handle(chatId, text);
    }
}
