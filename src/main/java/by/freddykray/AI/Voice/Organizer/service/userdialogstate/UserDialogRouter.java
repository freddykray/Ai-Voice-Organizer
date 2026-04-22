package by.freddykray.AI.Voice.Organizer.service.userdialogstate;

import by.freddykray.AI.Voice.Organizer.model.DialogState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDialogRouter {

    private final List<UserDialogHandler> handlers;

    public String route(long chatId, String text, DialogState state) {
        for (UserDialogHandler handler : handlers) {
            if (handler.canHandle(state)) {
                return handler.handle(chatId, text);
            }
        }
        return "Что-то пошло не так";
    }
}
