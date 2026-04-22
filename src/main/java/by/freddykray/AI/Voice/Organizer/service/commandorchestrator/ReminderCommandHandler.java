package by.freddykray.AI.Voice.Organizer.service.commandorchestrator;

import by.freddykray.AI.Voice.Organizer.dto.reminder.RequestReminderDto;
import by.freddykray.AI.Voice.Organizer.llm.LlmService;
import by.freddykray.AI.Voice.Organizer.llm.PromptType;
import by.freddykray.AI.Voice.Organizer.mapper.ReminderMapper;
import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import by.freddykray.AI.Voice.Organizer.service.reminder.ReminderService;
import by.freddykray.AI.Voice.Organizer.service.telegramuser.TelegramUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReminderCommandHandler implements CommandHandler {

    private final LlmService llmService;
    private final TelegramUserService telegramUserService;
    private final ReminderService reminderService;
    private final ReminderMapper reminderMapper;

    @Override
    public ParsedCommandType supports() {
        return ParsedCommandType.REMINDER;
    }

    @Override
    public String handle(long chatId, String text) {
        RequestReminderDto dto =
                llmService.parseAnswerLLM(text, PromptType.PARSE_REMINDER, RequestReminderDto.class);

        int remindBefore = telegramUserService.getHoursRemind(chatId);
        String result = reminderService.create(
                reminderMapper.toModelRequestReminder(chatId, remindBefore, dto)
        );

        if ("ASK_REMINDER_TIME".equals(result)) {
            return "ASK_REMINDER_TIME";
        }

        return result;
    }
}