package by.freddykray.AI.Voice.Organizer.service.task;

import by.freddykray.AI.Voice.Organizer.model.task.RequestTask;
import by.freddykray.AI.Voice.Organizer.repository.TaskRepository;
import by.freddykray.AI.Voice.Organizer.repository.UserDialogStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

    private final UserDialogStateRepository userDialogRepository;
    private final TaskRepository taskRepository;

    public String createTask(RequestTask request) {
        if (request.getDeadline() == null) {
            userDialogRepository.saveTempTaskWithoutDeadline(request.getTitle(), request.getChatId());
            return "ASK_DEADLINE";
        }

        taskRepository.create(request);
        return "Задача: " + request.getTitle() + " создана.\n" +
                "Дедлайн: " + request.getDeadline();
    }

}
