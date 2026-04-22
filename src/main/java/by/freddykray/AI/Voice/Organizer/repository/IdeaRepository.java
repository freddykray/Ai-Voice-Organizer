package by.freddykray.AI.Voice.Organizer.repository;

import by.freddykray.AI.Voice.Organizer.model.idea.Idea;
import by.freddykray.AI.Voice.Organizer.model.idea.RequestIdea;
import by.freddykray.AI.Voice.Organizer.model.idea.ResponseIdea;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static by.freddykray.jooq.generated.tables.Idea.IDEA;

@Repository
@AllArgsConstructor
public class IdeaRepository {

    private DSLContext dsl;

    public Idea create(RequestIdea request) {
        return dsl.insertInto(IDEA)
                .set(IDEA.CHAT_ID, request.getChatId())
                .set(IDEA.TITLE, request.getTitle())
                .set(IDEA.DESCRIPTION, request.getDescription())
                .set(IDEA.CREATED_AT, OffsetDateTime.now(ZoneId.of("Europe/Moscow")))
                .returning().fetchOneInto(Idea.class);
    }

    public List<ResponseIdea> getAllIdeaUser(long chatId) {
        return dsl.selectFrom(IDEA).where(IDEA.CHAT_ID.eq(chatId))
                .orderBy(IDEA.CREATED_AT.desc())
                .fetchInto(ResponseIdea.class);

    }

    public void delete(long ideaId) {
        dsl.delete(IDEA)
                .where(IDEA.ID.eq(ideaId))
                .execute();
    }
}
