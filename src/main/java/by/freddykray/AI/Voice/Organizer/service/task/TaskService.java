package by.freddykray.AI.Voice.Organizer.service.task;

import by.freddykray.AI.Voice.Organizer.model.task.RequestTask;
import by.freddykray.AI.Voice.Organizer.model.task.ResponseTask;
import by.freddykray.AI.Voice.Organizer.repository.TaskRepository;
import by.freddykray.AI.Voice.Organizer.service.DateTextFormatter;
import by.freddykray.AI.Voice.Organizer.service.userdialogstate.UserDialogStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final UserDialogStateService userDialogService;
    private final TaskRepository taskRepository;
    private final DateTextFormatter deadlineFormatter;


    public String createTask(RequestTask request) {
        if (request.getDeadline() == null) {
            userDialogService.saveTempTaskWithoutDeadline(request.getTitle(), request.getChatId());
            return "ASK_DEADLINE";
        }

        taskRepository.create(request);
        return "Задача: " + request.getTitle() + " создана.\n" +
                "Дедлайн: " + deadlineFormatter.formatDeadline(request.getDeadline(), request.isHasExactTime());
    }
    public void deleteTask(long taskId) {
        taskRepository.deleteTask(taskId);
    }

    public List<ResponseTask> sendUserTasks(long chatId) {
        return taskRepository.getAllTaskUser(chatId);
    }

    public void completeTask(long taskId) {
        taskRepository.completeTask(taskId);
    }


}
