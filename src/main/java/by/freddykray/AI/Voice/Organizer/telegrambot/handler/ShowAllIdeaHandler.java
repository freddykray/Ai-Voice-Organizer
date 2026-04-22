package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.model.idea.ResponseIdea;
import by.freddykray.AI.Voice.Organizer.model.reminder.ResponseReminder;
import by.freddykray.AI.Voice.Organizer.service.DateTextFormatter;
import by.freddykray.AI.Voice.Organizer.service.idea.IdeaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@AllArgsConstructor
public class ShowAllIdeaHandler implements BotTextHandler {

    private final DateTextFormatter dateTextFormatter;
    private final MessageSender messageSender;
    private final IdeaService ideaService;

    @Override
    public boolean canHandle(String text) {
        return "Мои идеи".equals(text);
    }

    @Override
    public void handle(long chatId, String text) {
        List<ResponseIdea> listIdea = ideaService.getAllIdeaUser(chatId);

        if (listIdea.isEmpty()) {
            messageSender.send(chatId, "У вас пока нет идей.");
            return;
        }
        for (ResponseIdea idea : listIdea) {

            String ideaText = buildIdeaText(idea);
            SendMessage message = new SendMessage(String.valueOf(chatId), ideaText);
            message.setReplyMarkup(buildReminderKeyboard(idea.getId()));
            messageSender.send(message);
        }

    }

    private String buildIdeaText(ResponseIdea idea) {

        return """
                Название идеи: %s
                Подробное описание: %s
                """.formatted(idea.getTitle(), idea.getDescription());
    }

    private InlineKeyboardMarkup buildReminderKeyboard(long ideaId) {
        InlineKeyboardButton doneButton = new InlineKeyboardButton("❌Удалить"); //todo изменить идею
        doneButton.setCallbackData("delete_idea:" + ideaId);
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(doneButton);
        return new InlineKeyboardMarkup(List.of(row));
    }
}
