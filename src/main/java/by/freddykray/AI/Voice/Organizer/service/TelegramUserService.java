package by.freddykray.AI.Voice.Organizer.service;

import by.freddykray.AI.Voice.Organizer.repository.TelegramUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public void ensureUserExists(long chatId) {
        telegramUserRepository.ensureUserExists(chatId);
    }

    public void updateRemindBefore(long chatId, int hour) {
        telegramUserRepository.updateRemindBefore(chatId, hour);
    }

    public Integer getHoursRemind(long chatId) {
       return telegramUserRepository.getHoursRemind(chatId);
    }


}
