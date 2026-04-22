package by.freddykray.AI.Voice.Organizer.service.shedul;

import by.freddykray.AI.Voice.Organizer.model.reminder.Reminder;
import by.freddykray.AI.Voice.Organizer.model.task.Task;
import by.freddykray.AI.Voice.Organizer.repository.ReminderRepository;
import by.freddykray.AI.Voice.Organizer.repository.TaskRepository;
import by.freddykray.AI.Voice.Organizer.service.DateTextFormatter;
import by.freddykray.AI.Voice.Organizer.telegrambot.handler.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShedulService {

    private final TaskRepository taskRepository;
    private final MessageSender messageSender;
    private final DateTextFormatter dateTextFormatter;
    private final ReminderRepository reminderRepository;

    public void sendDueTask() {
        List<Task> tasks = taskRepository.findTasksForReminder();

        for (Task task : tasks) {
            String deadlineText = dateTextFormatter.formatDeadline(
                    task.getDeadline(),
                    task.isHasExactTime()
            );

            String text = """
                    ⏰ Напоминание о задаче
               
                    Задача: %s
                    Срок: %s
                    """.formatted(task.getTitle(), deadlineText);

            messageSender.send(task.getUserId(), text);
            taskRepository.markReminderSent(task.getId());
        }
    }

    public void sendDueReminders() {
        List<Reminder> reminders = reminderRepository.findRemindersToSend();
        for (Reminder reminder : reminders) {
            String reminderTime = dateTextFormatter.formatReminder(
                    reminder.getDeadline(),
                    reminder.isHasExactTime()
            );
            String text = """
                    ⏰ Напоминание

                    %s
                    Время: %s
                    """.formatted(reminder.getTitle(), reminderTime);

            messageSender.send(reminder.getChatId(), text);
            reminderRepository.deleteReminder(reminder.getId());
        }

    }
}