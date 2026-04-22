package by.freddykray.AI.Voice.Organizer.repository;

import by.freddykray.AI.Voice.Organizer.model.reminder.Reminder;
import by.freddykray.AI.Voice.Organizer.model.reminder.RequestReminder;
import by.freddykray.AI.Voice.Organizer.model.task.RequestTask;
import by.freddykray.AI.Voice.Organizer.model.task.Task;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static by.freddykray.jooq.generated.tables.Reminder.REMINDER;

@Repository
@AllArgsConstructor
public class ReminderRepository {

    private DSLContext dsl;

    public Reminder create(RequestReminder request) {

        return dsl.insertInto(REMINDER)
                .set(REMINDER.CHAT_ID, request.getChatId())
                .set(REMINDER.TITLE, request.getTitle())
                .set(REMINDER.DESCRIPTION, request.getDescription())
                .set(REMINDER.DEADLINE, request.getDeadline().atOffset(ZoneOffset.UTC))
                .set(REMINDER.HAS_EXACT_TIME, request.isHasExactTime())
                .set(REMINDER.REMIND_AT, calculateRemindAt(request))
                .set(REMINDER.REMINDER_SENT, false)
                .set(REMINDER.CREATED_AT, Instant.now().atOffset(ZoneOffset.UTC))
                .returning()
                .fetchOneInto(Reminder.class);

    }

    private OffsetDateTime calculateRemindAt(RequestReminder request) {
        if (!request.isHasExactTime()) {
            return null;
        }

        Instant deadline = request.getDeadline();
        if (deadline == null) {
            return null;
        }
        Instant remindAt = deadline.minus(request.getRemindBefore(), ChronoUnit.HOURS);
        return remindAt.atOffset(ZoneOffset.UTC);
    }
}
