package by.freddykray.AI.Voice.Organizer.model;

import lombok.Data;

@Data
public class TelegramUser {

    private long chatId;
    private int remindBeforeHours;
}
