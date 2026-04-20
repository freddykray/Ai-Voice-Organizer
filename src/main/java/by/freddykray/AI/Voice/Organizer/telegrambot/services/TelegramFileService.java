package by.freddykray.AI.Voice.Organizer.telegrambot.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class TelegramFileService {

    private final TelegramClient telegramClient;
    private final String token;

    public TelegramFileService(@Value("${telegram.bot.token}") String token) {
        this.token = token;
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    public File getVoiceFile(String fileId) {
        GetFile getFile = new GetFile(fileId);
        try {
            return telegramClient.execute(getFile);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Path downloadVoiceFile(String filePath) {
        String url = String.format("https://api.telegram.org/file/bot%s/%s", token, filePath);
        Path targetPath = Path.of("voice_" + System.currentTimeMillis() + ".ogg");
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при скачивании файла", e);
        }
    }
}
