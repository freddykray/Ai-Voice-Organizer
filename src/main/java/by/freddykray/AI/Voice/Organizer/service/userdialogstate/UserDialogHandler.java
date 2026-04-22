package by.freddykray.AI.Voice.Organizer.service.userdialogstate;

import by.freddykray.AI.Voice.Organizer.model.DialogState;
import org.springframework.stereotype.Component;

@Component
public interface UserDialogHandler {

    boolean canHandle(DialogState dialogState);

    String handle(long chatId, String text);
}
