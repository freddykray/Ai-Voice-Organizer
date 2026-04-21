package by.freddykray.AI.Voice.Organizer.service;

import by.freddykray.AI.Voice.Organizer.dto.RequestTypeDto;
import by.freddykray.AI.Voice.Organizer.dto.task.ParsedDeadlineDto;
import by.freddykray.AI.Voice.Organizer.dto.task.RequestTaskDto;
import by.freddykray.AI.Voice.Organizer.llm.LlmService;
import by.freddykray.AI.Voice.Organizer.llm.PromptType;
import by.freddykray.AI.Voice.Organizer.mapper.TaskMapper;
import by.freddykray.AI.Voice.Organizer.model.DialogState;
import by.freddykray.AI.Voice.Organizer.model.UserDialogState;
import by.freddykray.AI.Voice.Organizer.service.task.TaskService;
import by.freddykray.AI.Voice.Organizer.service.userdialogstate.UserDialogStateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CommandOrchestrator {

    private final LlmService llmService;
    private final TaskService taskService;
    private final UserDialogStateService userDialogStateService;
    private final TaskMapper taskMapper;

    public String processMessage(long chatId, String text) {
        if (userDialogStateService.checkStateDialog(chatId)) {
            return handlePendingDialog(chatId, text);
        }
        return routeCommand(chatId, text);
    }

    public String routeCommand(long chatId, String text) {
        RequestTypeDto typeDto =
                llmService.parseAnswerLLM(text, PromptType.DETECT_TYPE, RequestTypeDto.class);

        return switch (typeDto.getType()) {
            case CHAT -> """
                    Я не для обычного общения. Я помогаю управлять задачами, напоминаниями и идеями.
                    Что можно сделать:
                    — создать задачу
                    — добавить напоминание
                    — сохранить идею
                    """;

            case TASK -> handleTask(chatId, text);
            case REMINDER -> "Напоминания добавим следующим шагом.";
            case IDEA -> "Идеи добавим следующим шагом.";
        };
    }

    private String handleTask(long chatId, String text) {
        RequestTaskDto dto =
                llmService.parseAnswerLLM(text, PromptType.PARSE_TASK, RequestTaskDto.class);

        String result = taskService.createTask(taskMapper.toModelRequestTask(chatId, dto));

        if ("ASK_DEADLINE".equals(result)) {
            return "ASK_DEADLINE";
        }

        return result;
    }

    private String handlePendingDialog(long chatId, String text) {

        UserDialogState state = userDialogStateService.getOne(chatId);

        if (state.getState() == DialogState.WAITING_FOR_DEADLINE) {
            return handleDeadlineAnswer(chatId, text, state);
        }
        return "Что то пошло не так";
    }

    private String handleDeadlineAnswer(long chatId, String text, UserDialogState state) {
        ParsedDeadlineDto deadlineDto =
                llmService.parseAnswerLLM(text, PromptType.PARSE_DEADLINE, ParsedDeadlineDto.class);
        if (deadlineDto.getDeadline() == null) {
            return "Не смог понять дедлайн. Напиши его, пожалуйста, например: \"завтра\", \"на пятницу\" или \"завтра в 15:00\".";
        }
        String result = taskService.createTask(taskMapper.toRequestTask(chatId, state.getPayload(), deadlineDto));
        userDialogStateService.deleteTempTaskWithoutDeadline(chatId);
        return result;
    }


}
