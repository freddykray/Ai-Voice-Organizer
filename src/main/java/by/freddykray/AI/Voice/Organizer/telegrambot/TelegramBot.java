package by.freddykray.AI.Voice.Organizer.telegrambot;

import by.freddykray.AI.Voice.Organizer.service.CommandOrchestrator;
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

import java.nio.file.Path;

@Slf4j
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    @Autowired
    private CommandOrchestrator commandOrchestrator;

    @Autowired
    private TelegramFileService fileService;

    @Autowired
    private SpeechToTextService stt;

    public TelegramBot(@Value("${telegram.bot.token}") String token) {
        telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(Update update) {
        if (!hasProcessableMessage(update)) {
            return;
        }
        long chatId = update.getMessage().getChatId();
        String text = extractText(update);

        if (text == null) {
            return;
        }

        String response = commandOrchestrator.processMessage(chatId, text);

        if ("ASK_DEADLINE".equals(response)) {
            askDeadline(chatId);
            return;
        }
        createAndSendMessage(chatId, response);
    }

    private String extractText(Update update) {
        if (update.getMessage().hasVoice()) {
            Path downloadedVoiceFile = downloadVoiceFile(update);
            String text = stt.transcribeOgg(downloadedVoiceFile);
            log.info("Распознан текст: {}", text);
            return text;
        }

        if (update.getMessage().hasText()) {
            return update.getMessage().getText();
        }

        return null;
    }

    public Path downloadVoiceFile(Update update) {
        String fileId = update.getMessage().getVoice().getFileId();
        File fileVoice = fileService.getVoiceFile(fileId);

        log.info("Получен filePath голосового сообщения из Telegram: {}", fileVoice.getFilePath());

        Path downloadedFile = fileService.downloadVoiceFile(fileVoice.getFilePath());

        log.info("Голосовое сообщение скачано локально: {}", downloadedFile);

        return downloadedFile;
    }

    private boolean hasProcessableMessage(Update update) {
        return update.hasMessage()
                && (update.getMessage().hasVoice() || update.getMessage().hasText());
    }

    public void askDeadline(long chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Укажи дедлайн для задачи. ")
                .build();

        sendMessage(message);
    }

    public void createAndSendMessage(long chatId, String text) {
        SendMessage message = createMessage(chatId, text);
        sendMessage(message);
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessage createMessage(long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

}
