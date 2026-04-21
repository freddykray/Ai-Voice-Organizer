package by.freddykray.AI.Voice.Organizer.service.userdialogstate;

import by.freddykray.AI.Voice.Organizer.model.UserDialogState;
import by.freddykray.AI.Voice.Organizer.repository.UserDialogStateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDialogStateService {

    private final UserDialogStateRepository userDialogStateRepository;

    public boolean checkStateDialog(long chatId) {
        return userDialogStateRepository.isCheckRecordUser(chatId);
    }

    public UserDialogState getOne(long chatId) {
        return userDialogStateRepository.getOne(chatId);
    }

    public void deleteTempTaskWithoutDeadline(long chatId) {
        userDialogStateRepository.deleteTempTaskWithoutDeadline(chatId);
    }
}
