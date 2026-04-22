package by.freddykray.AI.Voice.Organizer.telegrambot.handler.callbackhandler;

import by.freddykray.AI.Voice.Organizer.service.task.TaskService;
import by.freddykray.AI.Voice.Organizer.telegrambot.handler.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTaskCallbackHandler implements BotCallbackHandler {

    private final TaskService taskService;
    private final MessageSender messageSender;


    @Override
    public boolean canHandle(String callbackData) {
        return callbackData != null && callbackData.startsWith("delete_task:");
    }

    @Override
    public void handle(long chatId, String callbackData) {
        long taskId = Long.parseLong(callbackData.substring("delete_task:".length()));
        taskService.deleteTask(taskId);
        messageSender.send(chatId, "Задача успешно удалена! 😊");
    }
}
