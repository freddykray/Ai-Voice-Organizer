package by.freddykray.AI.Voice.Organizer.llm;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Component
public class PromptRegistry {

    private final Map<PromptType, String> staticPrompts = Map.of(
            PromptType.DETECT_TYPE, """
                    Ты определяешь тип сообщения пользователя для task manager.
                    Верни только JSON.
                    Формат:
                    {
                      "type": "TASK | REMINDER | IDEA | CHAT"
                    }

                    Правила:
                    - TASK — если пользователь хочет создать или изменить задачу
                    - REMINDER — если пользователь хочет создать напоминание
                    - IDEA — если пользователь формулирует идею
                    - CHAT — если это обычный разговор, не связанный с управлением задачами
                    - не добавляй другие поля
                    - не добавляй текст вне JSON
                    """
    );

    public String get(PromptType type) {
        return switch (type) {
            case DETECT_TYPE -> staticPrompts.get(type);
            case PARSE_TASK -> buildParseTaskPrompt();
            case PARSE_DEADLINE -> buildParseDeadlinePrompt();
            default -> throw new IllegalArgumentException("Неизвестный тип prompt: " + type);
        };
    }

    private String buildParseTaskPrompt() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        return """
                Ты парсер команды пользователя для создания задачи.

                Текущая дата и время: %s
                Часовой пояс: UTC

                Верни только JSON.
                Без пояснений.
                Без текста вне JSON.
                Без дополнительных полей.

                Формат ответа:
                {
                  "type": "TASK",
                  "title": "string",
                  "deadline": "ISO-8601 UTC string | null",
                  "hasExactTime": true
                }

                Правила:
                - type всегда TASK
                - title — краткая и точная суть задачи
                - deadline:
                  - если пользователь указал дату и время, верни строку в формате ISO-8601 UTC
                  - если пользователь указал только дату без времени, всё равно верни строку в формате ISO-8601 UTC
                  - если пользователь не указал дедлайн, верни null
                - hasExactTime:
                  - true, если пользователь указал точное время
                  - false, если пользователь указал только дату или не указал дедлайн
                - используй текущую дату и время как точку отсчёта для слов "завтра", "послезавтра", "в пятницу", "через час"
                - если указана только дата без времени, ставь время 00:00:00Z
                - не придумывай лишние данные
                - не добавляй другие поля
                - не добавляй текст вне JSON
                """.formatted(now);
    }

    private String buildParseDeadlinePrompt() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        return """
                Ты парсер дедлайна для task manager.

                Текущая дата и время: %s
                Часовой пояс: UTC

                Верни только JSON.
                Без пояснений.
                Без текста вне JSON.
                Без дополнительных полей.

                Формат ответа:
                {
                  "deadline": "ISO-8601 UTC string | null",
                  "hasExactTime": true
                }

                Правила:
                - deadline:
                  - если пользователь указал дату и время, верни строку в формате ISO-8601 UTC
                  - если пользователь указал только дату без времени, всё равно верни строку в формате ISO-8601 UTC
                  - если дедлайн не распознан, верни null
                - hasExactTime = true, если пользователь указал точное время
                - hasExactTime = false, если указана только дата без времени или дедлайн не распознан
                - используй текущую дату и время как точку отсчёта для слов "завтра", "послезавтра", "в пятницу", "через час"
                - если указана только дата без времени, ставь время 00:00:00Z
                - не добавляй другие поля
                - не добавляй текст вне JSON
                """.formatted(now);
    }
}