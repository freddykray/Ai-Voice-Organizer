package by.freddykray.AI.Voice.Organizer.service.commandorchestrator;

import by.freddykray.AI.Voice.Organizer.dto.task.RequestTaskDto;
import by.freddykray.AI.Voice.Organizer.llm.LlmService;
import by.freddykray.AI.Voice.Organizer.llm.PromptType;
import by.freddykray.AI.Voice.Organizer.mapper.TaskMapper;
import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import by.freddykray.AI.Voice.Organizer.service.TelegramUserService;
import by.freddykray.AI.Voice.Organizer.service.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TaskCommandHandler implements CommandHandler {

    private final LlmService llmService;
    private final TelegramUserService telegramUserService;
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Override
    public ParsedCommandType supports() {
        return ParsedCommandType.TASK;
    }

    @Override
    public String handle(long chatId, String text) {
        RequestTaskDto dto =
                llmService.parseAnswerLLM(text, PromptType.PARSE_TASK, RequestTaskDto.class);

        int remindBefore = telegramUserService.getHoursRemind(chatId);
        String result = taskService.createTask(
                taskMapper.toModelRequestTask(chatId, remindBefore, dto)
        );

        if ("ASK_DEADLINE".equals(result)) {
            return "ASK_DEADLINE";
        }

        return result;
    }
}
