package by.freddykray.AI.Voice.Organizer.repository;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static by.freddykray.jooq.generated.tables.TelegramUser.TELEGRAM_USER;

@Repository
@AllArgsConstructor
public class TelegramUserRepository {

    private final DSLContext dsl;

    public void ensureUserExists(long chatId) {
        dsl.insertInto(TELEGRAM_USER)
                .set(TELEGRAM_USER.CHAT_ID, chatId)
                .set(TELEGRAM_USER.REMIND_BEFORE_HOURS, 3)
                .onConflict(TELEGRAM_USER.CHAT_ID)
                .doNothing()
                .execute();
    }

    public void updateRemindBefore(long chatId, int hour) {
        dsl.update(TELEGRAM_USER)
                .set(TELEGRAM_USER.REMIND_BEFORE_HOURS, hour)
                .where(TELEGRAM_USER.CHAT_ID.eq(chatId))
                .execute();

    }

    public Integer getHoursRemind(long chatId) {
        return dsl.select(TELEGRAM_USER.REMIND_BEFORE_HOURS)
                .from(TELEGRAM_USER)
                .where(TELEGRAM_USER.CHAT_ID.eq(chatId))
                .fetchOne(TELEGRAM_USER.REMIND_BEFORE_HOURS);
    }
}
