package by.freddykray.AI.Voice.Organizer.repository;

import by.freddykray.AI.Voice.Organizer.model.task.RequestTask;
import by.freddykray.AI.Voice.Organizer.model.task.ResponseTask;
import by.freddykray.AI.Voice.Organizer.model.task.Task;
import by.freddykray.AI.Voice.Organizer.model.task.TaskStatus;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static by.freddykray.jooq.generated.tables.Task.TASK;

@Repository
@AllArgsConstructor
public class TaskRepository {

    private final DSLContext dsl;

    public Task create(RequestTask request) {
        return dsl.insertInto(TASK)
                .set(TASK.USER_ID, request.getChatId())
                .set(TASK.TITLE, request.getTitle())
                .set(TASK.DEADLINE, request.getDeadline().atZone(ZoneId.of("Europe/Moscow")).toOffsetDateTime())
                .set(TASK.HAS_EXACT_TIME, request.isHasExactTime())
                .set(TASK.STATUS, TaskStatus.ACTIVE.name())
                .set(TASK.REMIND_AT, calculateRemindAt(request))
                .set(TASK.REMINDER_SENT, false)
                .set(TASK.CREATED_AT, OffsetDateTime.now(ZoneId.of("Europe/Moscow")))
                .returning()
                .fetchOneInto(Task.class);
    }

    public void deleteTask(long taskId) {
        dsl.deleteFrom(TASK)
                .where(TASK.ID.eq(taskId))
                .execute();
    }

    public List<ResponseTask> getAllTaskUser(long chatId) {
        return dsl.selectFrom(TASK)
                .where(TASK.USER_ID.eq(chatId))
                .and(TASK.STATUS.eq(TaskStatus.ACTIVE.name()))
                .orderBy(TASK.DEADLINE.asc().nullsLast())
                .fetchInto(ResponseTask.class);
    }

    public List<Task> findTasksForReminder() {
        return dsl.selectFrom(TASK)
                .where(TASK.REMIND_AT.isNotNull())
                .and(TASK.REMINDER_SENT.eq(false))
                .and(TASK.STATUS.eq(TaskStatus.ACTIVE.name()))
                .and(TASK.REMIND_AT.le(OffsetDateTime.now(ZoneId.of("Europe/Moscow"))))
                .fetchInto(Task.class);
    }

    public void markReminderSent(long taskId) {
        dsl.update(TASK)
                .set(TASK.REMINDER_SENT, true)
                .where(TASK.ID.eq(taskId))
                .execute();
    }

    public void completeTask(long taskId) {
        dsl.update(TASK)
                .set(TASK.STATUS, TaskStatus.DONE.name())
                .where(TASK.ID.eq(taskId))
                .execute();
    }

    private OffsetDateTime calculateRemindAt(RequestTask request) {
        if (!request.isHasExactTime()) {
            return null;
        }

        Instant deadline = request.getDeadline();
        if (deadline == null) {
            return null;
        }
        Instant remindAt = deadline.minus(request.getRemindBefore(), ChronoUnit.HOURS);
        return remindAt.atZone(ZoneId.of("Europe/Moscow")).toOffsetDateTime();
    }
}
