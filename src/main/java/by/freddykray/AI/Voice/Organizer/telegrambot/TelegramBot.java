package by.freddykray.AI.Voice.Organizer.telegrambot;

import by.freddykray.AI.Voice.Organizer.dto.ParsedTaskDto;
import by.freddykray.AI.Voice.Organizer.llm.LlmService;
import by.freddykray.AI.Voice.Organizer.telegrambot.services.SpeechToTextService;
import by.freddykray.AI.Voice.Organizer.telegrambot.services.TelegramFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    @Autowired
    private SpeechToTextService stt;

    @Autowired
    private TelegramFileService fileService;

    @Autowired
    private LlmService llmService;

    public TelegramBot(@Value("${telegram.bot.token}") String token) {
        telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            SendMessage message = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Ты написал: " + text)
                    .build();
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasMessage() && update.getMessage().hasVoice()) {
            Path downloadedVoiceFile = downloadVoiceFile(update);
            String textFromVoiceFile = stt.transcribeOgg(downloadedVoiceFile);
            log.info("Распознан текст: {}", textFromVoiceFile);
            ParsedTaskDto parsedTask = llmService.parseTaskCommand(textFromVoiceFile);

            createAndSendMessage(update, textFromVoiceFile, parsedTask.toString());

        }

    }


    public Path downloadVoiceFile(Update update) {
        String fileId = update.getMessage().getVoice().getFileId();
        File fileVoice = fileService.getVoiceFile(fileId);

        log.info("Получен filePath голосового сообщения из Telegram: {}", fileVoice.getFilePath());

        Path downloadedFile = fileService.downloadVoiceFile(fileVoice.getFilePath());

        log.info("Голосовое сообщение скачано локально: {}", downloadedFile);

        return downloadedFile;
    }

    public void createAndSendMessage(Update update, String text, String textllm) {
        SendMessage message = createMessage(update, text, textllm);
        sendMessage(message);
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessage createMessage(Update update, String text, String textllm) {
        Long chatId = update.getMessage().getChatId();
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Это голосовое сообщение " + text + "\n А это от ллм: " + textllm)
                .build();
    }

}
