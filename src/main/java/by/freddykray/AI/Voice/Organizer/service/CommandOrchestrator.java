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
    private final TelegramUserService telegramUserService;

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

        int remindBefore = telegramUserService.getHoursRemind(chatId);
        String result = taskService.createTask(taskMapper.toModelRequestTask(chatId, remindBefore, dto));

        if ("ASK_DEADLINE".equals(result)) {
            return "ASK_DEADLINE";
        }

        return result;
    }

    private String handlePendingDialog(long chatId, String text) {

        UserDialogState state = userDialogStateService.getOne(chatId);

        if (state.getState() == DialogState.WAITING_FOR_DEADLINE) {
            return handleDeadlineAnswer(chatId, text, state);
        } else if (state.getState() == DialogState.WAITING_REMIND_HOURS) {
            return handleRemindHoursInput(chatId, text);
        }
        return "Что то пошло не так";
    }

    private String handleRemindHoursInput(long chatId, String text) {
        try {
            int hours = Integer.parseInt(text);

            if (hours <= 0) {
                return "Число не может быть отрицательным. Напиши значение от 1 до 23.";

            }
            if (hours > 23) {
                return "Слишком большое значение. Напиши число от 0 до 23.";
            }

            telegramUserService.updateRemindBefore(chatId, hours);
            userDialogStateService.deleteTempRecord(chatId);
            return "Готово. Теперь я буду предупреждать за " + hours + " ч.";
        } catch (NumberFormatException e) {
            return "Нужно отправить именно число от 0 до 23. Например: 2";
        }
    }

    private String handleDeadlineAnswer(long chatId, String text, UserDialogState state) {
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
