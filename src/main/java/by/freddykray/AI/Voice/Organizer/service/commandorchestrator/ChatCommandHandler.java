package by.freddykray.AI.Voice.Organizer.service.commandorchestrator;

import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import org.springframework.stereotype.Component;

@Component
public class ChatCommandHandler implements CommandHandler {

    @Override
    public ParsedCommandType supports() {
        return ParsedCommandType.CHAT;
    }

    @Override
    public String handle(long chatId, String text) {
        return """
                Я не для обычного общения. Я помогаю управлять задачами, напоминаниями и идеями.
                Что можно сделать:
                — создать задачу
                — добавить напоминание
                — сохранить идею
                """;
    }
}
