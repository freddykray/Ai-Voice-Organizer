package by.freddykray.AI.Voice.Organizer.telegrambot.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;

@Service
@AllArgsConstructor
@Slf4j
public class SpeechToTextService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${stt.url}")
    private String sttUrl;

    public String transcribeOgg(Path oggPath) {
        Path fileWav = convertOggToWav(oggPath);
        return transcribeVoice(fileWav);
    }

    private String transcribeVoice(Path audioPath) {
        HttpEntity<MultiValueMap<String, Object>> request = buildTranscriptionRequest(audioPath);
        String response = sendTranscriptionRequest(request);
        return extractTextFromResponse(response);
    }

    private HttpEntity<MultiValueMap<String, Object>> buildTranscriptionRequest(Path audioPath) {
        HttpHeaders headers = createMultipartHeaders();
        MultiValueMap<String, Object> body = createMultipartBody(audioPath);
        return new HttpEntity<>(body, headers);
    }

    private HttpHeaders createMultipartHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

    private MultiValueMap<String, Object> createMultipartBody(Path audioPath) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(audioPath.toFile()));
        return body;
    }

    private String sendTranscriptionRequest(HttpEntity<MultiValueMap<String, Object>> request) {
        return restTemplate.postForObject(sttUrl, request, String.class);
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("text").asText().trim();
        } catch (Exception e) {
            log.error("Ошибка при разборе ответа STT", e);
            throw new RuntimeException("Ошибка при разборе ответа STT", e);
        }
    }

    public Path convertOggToWav(Path inputPath) {
        Path outputPath = buildWavFilePath(inputPath);
        ProcessBuilder processBuilder = buildFfmpegProcess(inputPath, outputPath);
        runConversion(processBuilder);
        return outputPath;
    }

    public Path buildWavFilePath(Path inputPath) {
        String fileName = inputPath.getFileName().toString();
        String wavFileName = fileName.replace(".ogg", ".wav");

        Path parent = inputPath.toAbsolutePath().getParent();
        return parent.resolve(wavFileName);
    }

    private ProcessBuilder buildFfmpegProcess(Path inputPath, Path outputPath) {
        return new ProcessBuilder(
                "ffmpeg",
                "-i", inputPath.toAbsolutePath().toString(),
                "-ar", "16000",
                "-ac", "1",
                "-c:a", "pcm_s16le",
                outputPath.toString()
        );
    }

    private void runConversion(ProcessBuilder processBuilder) {
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                log.error("Ошибка конвертации файла в wav");
                throw new RuntimeException("Ошибка конвертации файла в wav");
            }
        } catch (Exception e) {
            log.error("Не удалось конвертировать .ogg в .wav", e);
            throw new RuntimeException("Не удалось конвертировать .ogg в .wav", e);
        }
    }
}
