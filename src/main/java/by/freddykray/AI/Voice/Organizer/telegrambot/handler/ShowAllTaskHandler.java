package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.model.task.ResponseTask;
import by.freddykray.AI.Voice.Organizer.service.DateTextFormatter;
import by.freddykray.AI.Voice.Organizer.service.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
@AllArgsConstructor
public class ShowAllTaskHandler implements BotTextHandler {

    private final TaskService taskService;
    private final DateTextFormatter dateTextFormatter;
    private final MessageSender messageSender;

    @Override
    public boolean canHandle(String text) {
        return "Мои задачи".equals(text);
    }

    @Override
    public void handle(long chatId, String text) {
        List<ResponseTask> tasks = taskService.sendUserTasks(chatId);

        if (tasks.isEmpty()) {
            messageSender.send(chatId, "У вас пока нет активных задач.");
            return;
        }
        for (ResponseTask task : tasks) {

            String taskText = buildTaskText(task);
            SendMessage message = new SendMessage(String.valueOf(chatId), taskText);
            message.setReplyMarkup(buildTaskKeyboard(task.getId()));
            messageSender.send(message);

        }
    }

    private String buildTaskText(ResponseTask task) {
        String deadlineText = dateTextFormatter.formatDeadline(
                task.getDeadline(),
                task.isHasExactTime()
        );

        return """
                Задача: %s
                Дедлайн: %s
                """.formatted(task.getTitle(), deadlineText);
    }

    private InlineKeyboardMarkup buildTaskKeyboard(long taskId) {
        InlineKeyboardButton doneButton = new InlineKeyboardButton("✅ Выполнена");
        doneButton.setCallbackData("done_task:" + taskId);
        InlineKeyboardButton deleteButton = new InlineKeyboardButton("🗑 Удалить");
        deleteButton.setCallbackData("delete_task:" + taskId);
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(doneButton);
        row.add(deleteButton);
        return new InlineKeyboardMarkup(List.of(row));
    }
}
