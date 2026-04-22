package by.freddykray.AI.Voice.Organizer.telegrambot;

import by.freddykray.AI.Voice.Organizer.model.DialogState;
import by.freddykray.AI.Voice.Organizer.service.userdialogstate.UserDialogRouter;
import by.freddykray.AI.Voice.Organizer.service.userdialogstate.UserDialogStateService;
import by.freddykray.AI.Voice.Organizer.telegrambot.handler.MessageSender;
import by.freddykray.AI.Voice.Organizer.telegrambot.handler.callbackhandler.CallbackRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateRouter {

    private final UserDialogStateService userDialogStateService;
    private final UserDialogRouter userDialogRouter;
    private final TextMessageRouter textMessageRouter;
    private final CallbackRouter callbackRouter;
    private final MessageSender telegramSender;

    public void routeText(long chatId, String text) {
        if (text == null || text.isBlank()) {
            return;
        }

        DialogState state = userDialogStateService.getStateDialogUser(chatId);

        if (state != null) {
            String response = userDialogRouter.route(chatId, text, state);
            telegramSender.send(chatId, response);
            return;
        }

        textMessageRouter.route(chatId, text);
    }

    public void routeCallback(long chatId, String callbackData, String callbackQueryId) {
        if (callbackData == null || callbackData.isBlank()) {
            return;
        }
        DialogState state = userDialogStateService.getStateDialogUser(chatId);
        if (state != null) {
            telegramSender.send(chatId, "Сначала ответь на предыдущий вопрос.");
            telegramSender.answerCallback(callbackQueryId);
            return;
        }
        callbackRouter.route(chatId, callbackData);
        telegramSender.answerCallback(callbackQueryId);
    }
}