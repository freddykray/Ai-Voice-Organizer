package by.freddykray.AI.Voice.Organizer.repository;

import by.freddykray.AI.Voice.Organizer.model.reminder.Reminder;
import by.freddykray.AI.Voice.Organizer.model.reminder.RequestReminder;
import by.freddykray.AI.Voice.Organizer.model.reminder.ResponseReminder;
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
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
                .set(REMINDER.DEADLINE, request.getDeadline().atZone(ZoneId.of("Europe/Moscow")).toOffsetDateTime())
                .set(REMINDER.HAS_EXACT_TIME, request.isHasExactTime())
                .set(REMINDER.REMIND_AT, calculateRemindAt(request))
                .set(REMINDER.REMINDER_SENT, false)
                .set(REMINDER.CREATED_AT, OffsetDateTime.now(ZoneId.of("Europe/Moscow")))
                .returning()
                .fetchOneInto(Reminder.class);

    }

    public List<ResponseReminder> getAllReminderUser(long chatId) {
        return dsl.selectFrom(REMINDER)
                .where(REMINDER.CHAT_ID.eq(chatId))
                .orderBy(REMINDER.DEADLINE.asc().nullsLast())
                .fetchInto(ResponseReminder.class);
    }

    public void deleteReminder(long reminderId) {
        dsl.delete(REMINDER)
                .where(REMINDER.ID.eq(reminderId))
                .execute();
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
        return remindAt.atZone(ZoneId.of("Europe/Moscow")).toOffsetDateTime();
    }
}
