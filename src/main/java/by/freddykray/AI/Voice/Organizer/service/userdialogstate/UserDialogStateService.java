package by.freddykray.AI.Voice.Organizer.service.userdialogstate;

import by.freddykray.AI.Voice.Organizer.model.DialogState;
import by.freddykray.AI.Voice.Organizer.model.UserDialogState;
import by.freddykray.AI.Voice.Organizer.repository.UserDialogStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDialogStateService {

    private final UserDialogStateRepository userDialogStateRepository;

    public DialogState getStateDialogUser(long chatId) {
        return userDialogStateRepository.getStateDialogUser(chatId);
    }

    public UserDialogState getOne(long chatId) {
        return userDialogStateRepository.getOne(chatId);
    }

    public void saveTempTaskWithoutDeadline(String payload, long chatId) {
        userDialogStateRepository.saveTempTaskWithoutDeadline(payload, chatId);
    }

    public void saveTempRemindHour(String payload, long chatId) {
        userDialogStateRepository.saveTempRemindHour(payload,chatId);
    }

    public void deleteTempRecord(long chatId) {
        userDialogStateRepository.deleteTempRecord(chatId);
    }
}
