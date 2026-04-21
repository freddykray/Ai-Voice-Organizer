package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.service.CommandOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultMessageHandler {

    private final CommandOrchestrator commandOrchestrator;
    private final MessageSender messageSender;

    public void handle(long chatId, String text) {
        if (text == null) {
            return;
        }

        String response = commandOrchestrator.processMessage(chatId, text);

        if ("ASK_DEADLINE".equals(response)) {
            messageSender.send(chatId, "Укажи дедлайн для задачи.");
            return;
        }

        messageSender.send(chatId, response);
    }
}
