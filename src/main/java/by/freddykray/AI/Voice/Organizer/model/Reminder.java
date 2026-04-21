package by.freddykray.AI.Voice.Organizer.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Reminder {

    private long chatId;
    private String title;
    private String description;
    private Instant deadline;
    private Instant remindAt;
    private boolean reminderSent;
    private Instant createdAt;
}
