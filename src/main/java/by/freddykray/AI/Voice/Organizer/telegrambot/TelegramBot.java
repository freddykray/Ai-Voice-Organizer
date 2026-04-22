package by.freddykray.AI.Voice.Organizer.telegrambot;

import by.freddykray.AI.Voice.Organizer.service.TelegramUserService;
import by.freddykray.AI.Voice.Organizer.telegrambot.services.SpeechToTextService;
import by.freddykray.AI.Voice.Organizer.telegrambot.services.TelegramFileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.file.Path;

@Slf4j
@Configuration
@AllArgsConstructor
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramFileService fileService;
    private final SpeechToTextService stt;
    private final TextMessageRouter textMessageRouter;

    @Override
    public void consume(Update update) {
        if (!hasProcessableMessage(update)) {
            return;
        }
        long chatId = update.getMessage().getChatId();
        String text = extractText(update);
        textMessageRouter.route(chatId, text);
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
}
