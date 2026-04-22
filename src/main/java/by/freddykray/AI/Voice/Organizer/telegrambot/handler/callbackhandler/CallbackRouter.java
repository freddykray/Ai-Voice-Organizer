package by.freddykray.AI.Voice.Organizer.telegrambot.handler.callbackhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CallbackRouter {

    private final List<BotCallbackHandler> handlers;

    public void route(long chatId, String callbackData) {
        for (BotCallbackHandler handler : handlers) {
            if (handler.canHandle(callbackData)) {
                handler.handle(chatId, callbackData);
                return;
            }
        }

        throw new IllegalArgumentException("Нет обработчика для callback: " + callbackData);
    }
}
