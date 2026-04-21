package by.freddykray.AI.Voice.Organizer.llm;

import by.freddykray.AI.Voice.Organizer.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${llm.url}")
    private String llmUrl;

    public LlmService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ParsedTaskDto parseTaskCommand(String text) {
        LlmRequestDto requestBody = buildRequest(text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LlmRequestDto> request = new HttpEntity<>(requestBody, headers);

        LlmResponseDto response = restTemplate.postForObject(
                llmUrl,
                request,
                LlmResponseDto.class
        );

        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()
                || response.getChoices().get(0).getMessage() == null
                || response.getChoices().get(0).getMessage().getContent() == null) {
            throw new RuntimeException("LLM вернула пустой ответ");
        }

        String content = response.getChoices().get(0).getMessage().getContent();

        try {
            return objectMapper.readValue(content, ParsedTaskDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON от LLM", e);
        }
    }

    private LlmRequestDto buildRequest(String text) {
        String systemPrompt = """
                Ты парсер команд для task manager.
                
                Верни только JSON, без пояснений и без текста вне JSON.
                
                Разрешены только поля:
                - type
                - title
                - deadline
                
                Формат:
                {
                  "type": "TASK | REMINDER | IDEA",
                  "title": "string",
                  "deadline": "string | null"
                }
                
                Правила:
                - type может быть только TASK, REMINDER или IDEA
                - title — краткая суть команды
                - deadline — срок в том виде, как его сказал пользователь
                - если в тексте есть указание времени или даты, обязательно заполни поле deadline
                - не преобразуй относительные даты в календарные
                - не придумывай данные
                - не добавляй другие поля
                - если срока нет, ставь null
                
                Примеры:
                
                Вход: "создай задачу на пятницу проверить авторизацию"
                Выход:
                {
                  "type": "TASK",
                  "title": "проверить авторизацию",
                  "deadline": "пятница"
                }
                
                Вход: "напомни завтра оплатить интернет"
                Выход:
                {
                  "type": "REMINDER",
                  "title": "оплатить интернет",
                  "deadline": "завтра"
                }
                
                Вход: "идея сделать отдельный экран статистики"
                Выход:
                {
                  "type": "IDEA",
                  "title": "сделать отдельный экран статистики",
                  "deadline": null
                }
                """;

        return new LlmRequestDto(
                "qwen/qwen3.5-9b",
                List.of(
                        new LlmMessageDto("system", systemPrompt),
                        new LlmMessageDto("user", text)
                ),
                0.0,
                100
        );
    }
}
