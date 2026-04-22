package by.freddykray.AI.Voice.Organizer.service.commandorchestrator;

import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;

public interface CommandHandler {
    ParsedCommandType supports();
    String handle(long chatId, String text);
}
