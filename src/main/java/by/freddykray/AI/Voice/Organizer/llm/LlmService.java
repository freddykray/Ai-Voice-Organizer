package by.freddykray.AI.Voice.Organizer.llm;

import by.freddykray.AI.Voice.Organizer.dto.llm.LlmMessageDto;
import by.freddykray.AI.Voice.Organizer.dto.llm.LlmRequestDto;
import by.freddykray.AI.Voice.Organizer.dto.llm.LlmResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class LlmService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PromptRegistry promptRegistry;

    @Value("${llm.url}")
    private String llmUrl;

    public LlmService(RestTemplate restTemplate, ObjectMapper objectMapper, PromptRegistry promptRegistry) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.promptRegistry = promptRegistry;
    }


    public <T> T parseAnswerLLM(String text, PromptType promptType, Class<T> dtoClass) {
        LlmRequestDto requestBody = buildRequest(text, promptType);
        LlmResponseDto response = createPostHttp(requestBody);
        checkAnswerNotNull(response);
        String content = response.getChoices().get(0).getMessage().getContent();
        try {
            return objectMapper.readValue(content, dtoClass);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON от LLM", e);
        }
    }

    private LlmRequestDto buildRequest(String text, PromptType promptType) {
        String systemPrompt = promptRegistry.get(promptType);
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

    private LlmResponseDto createPostHttp(LlmRequestDto requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LlmRequestDto> request = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(
                llmUrl,
                request,
                LlmResponseDto.class
        );
    }

    private void checkAnswerNotNull(LlmResponseDto response) {
        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()
                || response.getChoices().get(0).getMessage() == null
                || response.getChoices().get(0).getMessage().getContent() == null) {
            throw new RuntimeException("LLM вернула пустой ответ");
        }
    }

}
