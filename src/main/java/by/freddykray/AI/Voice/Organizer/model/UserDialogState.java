package by.freddykray.AI.Voice.Organizer.model;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDialogState {

    private long userId;
    private DialogState state;
    private String payload;
    private Instant createdAt;
}
