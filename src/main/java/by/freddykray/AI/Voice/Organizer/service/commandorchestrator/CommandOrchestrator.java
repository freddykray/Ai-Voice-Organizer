package by.freddykray.AI.Voice.Organizer.service.commandorchestrator;

import by.freddykray.AI.Voice.Organizer.dto.RequestTypeDto;
import by.freddykray.AI.Voice.Organizer.llm.LlmService;
import by.freddykray.AI.Voice.Organizer.llm.PromptType;
import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class CommandOrchestrator {

    private final LlmService llmService;
    private final Map<ParsedCommandType, CommandHandler> handlers;

    public CommandOrchestrator(LlmService llmService, List<CommandHandler> handlers) {
        this.llmService = llmService;
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(CommandHandler::supports, Function.identity()));
    }

    public String routeCommand(long chatId, String text) {
        RequestTypeDto typeDto =
                llmService.parseAnswerLLM(text, PromptType.DETECT_TYPE, RequestTypeDto.class);

        CommandHandler handler = handlers.get(typeDto.getType());

        if (handler == null) {
            throw new IllegalArgumentException("Не найден обработчик для типа: " + typeDto.getType());
        }

        return handler.handle(chatId, text);
    }
}
