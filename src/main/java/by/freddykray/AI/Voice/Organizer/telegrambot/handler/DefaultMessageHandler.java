package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.service.commandorchestrator.CommandOrchestrator;
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

        String response = commandOrchestrator.routeCommand(chatId, text);

        if ("ASK_DEADLINE".equals(response)) {
            messageSender.send(chatId, "Укажи дедлайн для задачи.");
            return;
        } else if ("ASK_REMINDER_TIME".equals(response)) {
            messageSender.send(chatId, "Укажи дедлайн для задачи.");
            return;
        }

        messageSender.send(chatId, response);
    }
}
