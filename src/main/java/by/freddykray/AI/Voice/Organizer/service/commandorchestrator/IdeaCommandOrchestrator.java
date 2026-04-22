package by.freddykray.AI.Voice.Organizer.service.commandorchestrator;

import by.freddykray.AI.Voice.Organizer.dto.idea.RequestIdeaDto;
import by.freddykray.AI.Voice.Organizer.llm.LlmService;
import by.freddykray.AI.Voice.Organizer.llm.PromptType;
import by.freddykray.AI.Voice.Organizer.mapper.IdeaMapper;
import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import by.freddykray.AI.Voice.Organizer.service.idea.IdeaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static by.freddykray.AI.Voice.Organizer.model.ParsedCommandType.IDEA;

@Component
@AllArgsConstructor
public class IdeaCommandOrchestrator implements CommandHandler {

    private final LlmService llmService;
    private final IdeaService ideaService;
    private final IdeaMapper ideaMapper;

    @Override
    public ParsedCommandType supports() {
        return IDEA;
    }

    @Override
    public String handle(long chatId, String text) {
        RequestIdeaDto dto =
                llmService.parseAnswerLLM(text, PromptType.PARSE_IDEA, RequestIdeaDto.class);
        return ideaService.create(ideaMapper.toModelRequestIdea(chatId, dto));
    }
}
