package by.freddykray.AI.Voice.Organizer.repository;

import by.freddykray.AI.Voice.Organizer.model.task.RequestTask;
import by.freddykray.AI.Voice.Organizer.model.task.Task;
import by.freddykray.AI.Voice.Organizer.model.task.TaskStatus;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;

import static by.freddykray.jooq.generated.tables.Task.TASK;

@Repository
@AllArgsConstructor
public class TaskRepository {

    private final DSLContext dsl;

    public Task create(RequestTask request) {
        return dsl.insertInto(TASK)
                .set(TASK.USER_ID, request.getChatId())
                .set(TASK.TITLE, request.getTitle())
                .set(TASK.DEADLINE, request.getDeadline().atOffset(ZoneOffset.UTC))
                .set(TASK.HAS_EXACT_TIME, request.isHasExactTime())
                .set(TASK.STATUS, TaskStatus.ACTIVE.name())
                .set(TASK.REMIND_AT, !request.isHasExactTime() ? null :  request.getRemindAt().atOffset(ZoneOffset.UTC))
                .set(TASK.REMINDER_SENT, false)
                .set(TASK.CREATED_AT, Instant.now().atOffset(ZoneOffset.UTC))
                .returning()
                .fetchOneInto(Task.class);
    }
}
