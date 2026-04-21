package by.freddykray.AI.Voice.Organizer.repository;

import by.freddykray.AI.Voice.Organizer.model.DialogState;
import by.freddykray.AI.Voice.Organizer.model.UserDialogState;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;

import static by.freddykray.jooq.generated.tables.UserDialogState.USER_DIALOG_STATE;

@Repository
@AllArgsConstructor
public class UserDialogStateRepository {

    private final DSLContext dsl;

    public void saveTempTaskWithoutDeadline(String title, long userId) {
        dsl.insertInto(USER_DIALOG_STATE)
                .set(USER_DIALOG_STATE.USER_ID, userId)
                .set(USER_DIALOG_STATE.STATE, DialogState.WAITING_FOR_DEADLINE.name())
                .set(USER_DIALOG_STATE.PAYLOAD, title)
                .set(USER_DIALOG_STATE.CREATED_AT, Instant.now().atOffset(ZoneOffset.UTC))
                .execute();

    }

    public void saveTempRemindHour(long userId) {
        dsl.insertInto(USER_DIALOG_STATE)
                .set(USER_DIALOG_STATE.USER_ID, userId)
                .set(USER_DIALOG_STATE.STATE, DialogState.WAITING_REMIND_HOURS.name())
                .set(USER_DIALOG_STATE.PAYLOAD, "Укажите время за сколько часов до дедлайна напоминать")
                .set(USER_DIALOG_STATE.CREATED_AT, Instant.now().atOffset(ZoneOffset.UTC))
                .execute();
    }

    public UserDialogState getOne(long chatId) {
        return dsl.selectFrom(USER_DIALOG_STATE)
                .where(USER_DIALOG_STATE.USER_ID.eq(chatId))
                .fetchOneInto(UserDialogState.class);
    }

    public boolean isCheckRecordUser(long chatId) {
        return dsl.selectFrom(USER_DIALOG_STATE)
                .where(USER_DIALOG_STATE.USER_ID.eq(chatId))
                .fetchOptional()
                .isPresent();
    }

    public void deleteTempRecord(long userId) {
        dsl.deleteFrom(USER_DIALOG_STATE)
                .where(USER_DIALOG_STATE.USER_ID.eq(userId))
                .execute();
    }
}
