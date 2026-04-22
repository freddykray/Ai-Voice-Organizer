package by.freddykray.AI.Voice.Organizer.service.userdialogstate;

import by.freddykray.AI.Voice.Organizer.model.DialogState;
import by.freddykray.AI.Voice.Organizer.service.TelegramUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static by.freddykray.AI.Voice.Organizer.model.DialogState.WAITING_REMIND_HOURS;

@AllArgsConstructor
@Component
public class RemindHoursInput implements UserDialogHandler {

    private final TelegramUserService telegramUserService;
    private final UserDialogStateService userDialogStateService;


    @Override
    public boolean canHandle(DialogState dialogState) {
        return WAITING_REMIND_HOURS.equals(dialogState);
    }

    @Override
    public String handle(long chatId, String text) {
        try {
            int hours = Integer.parseInt(text);
            if (hours <= 0) {
                return "Число не может быть отрицательным. Напиши значение от 1 до 23.";
            }
            if (hours > 23) {
                return "Слишком большое значение. Напиши число от 0 до 23.";
            }
            telegramUserService.updateRemindBefore(chatId, hours);
            userDialogStateService.deleteTempRecord(chatId);
            return "Готово. Теперь я буду предупреждать за " + hours + " ч.";
        } catch (NumberFormatException e) {
            return "Нужно отправить именно число от 0 до 23. Например: 2";
        }
    }
}
