package by.freddykray.AI.Voice.Organizer.service.userdialogstate;

import by.freddykray.AI.Voice.Organizer.dto.task.ParsedDeadlineDto;
import by.freddykray.AI.Voice.Organizer.llm.LlmService;
import by.freddykray.AI.Voice.Organizer.llm.PromptType;
import by.freddykray.AI.Voice.Organizer.mapper.TaskMapper;
import by.freddykray.AI.Voice.Organizer.model.DialogState;
import by.freddykray.AI.Voice.Organizer.model.UserDialogState;
import by.freddykray.AI.Voice.Organizer.service.telegramuser.TelegramUserService;
import by.freddykray.AI.Voice.Organizer.service.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static by.freddykray.AI.Voice.Organizer.model.DialogState.WAITING_FOR_DEADLINE;

@AllArgsConstructor
@Component
public class DeadlineAnswer implements UserDialogHandler {

    private final LlmService llmService;
    private final TaskService taskService;
    private final UserDialogStateService userDialogStateService;
    private final TaskMapper taskMapper;
    private final TelegramUserService telegramUserService;

    @Override
    public boolean canHandle(DialogState dialogState) {
        return WAITING_FOR_DEADLINE.equals(dialogState);
    }

    @Override
    public String handle(long chatId, String text) {
        UserDialogState state = userDialogStateService.getOne(chatId);

        ParsedDeadlineDto deadlineDto =
                llmService.parseAnswerLLM(text, PromptType.PARSE_DEADLINE, ParsedDeadlineDto.class);

        if (deadlineDto.getDeadline() == null) {
            return "Не смог понять дедлайн. Напиши его, пожалуйста, например: \"завтра\", \"на пятницу\" или \"завтра в 15:00\".";
        }

        int remindBefore = telegramUserService.getHoursRemind(chatId);
        String result = taskService.createTask(taskMapper.toRequestTask(chatId, remindBefore, state.getPayload(), deadlineDto));

        userDialogStateService.deleteTempRecord(chatId);
        return result;
    }
}
