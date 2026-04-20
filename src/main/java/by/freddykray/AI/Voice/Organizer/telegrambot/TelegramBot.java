package by.freddykray.AI.Voice.Organizer.telegrambot;

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
            createAndSendMessage(update, textFromVoiceFile);
            deleteVoice(downloadedVoiceFile);
        }

    }

    private void deleteVoice(Path oggPath) {
        Path wavPath = stt.convertOggToWav(oggPath);
        try {
            Files.deleteIfExists(oggPath);
        } catch (IOException e) {
            log.error("Не удалось удалить ogg файл: {}", oggPath, e);
        }

        try {
            Files.deleteIfExists(wavPath);
        } catch (IOException e) {
            log.error("Не удалось удалить wav файл: {}", wavPath, e);
        }
    }

    public Path downloadVoiceFile(Update update) {
        String fileId = update.getMessage().getVoice().getFileId();
        File fileVoice = fileService.getVoiceFile(fileId);
        return fileService.downloadVoiceFile(fileVoice.getFilePath());

    }

    public void createAndSendMessage(Update update, String text) {
        SendMessage message = createMessage(update, text);
        sendMessage(message);
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessage createMessage(Update update, String text) {
        Long chatId = update.getMessage().getChatId();
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Это голосовое сообщение " + text)
                .build();
    }

}
