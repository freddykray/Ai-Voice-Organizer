package by.freddykray.AI.Voice.Organizer.model.reminder;

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
    private boolean isHasExactTime;
    private Instant createdAt;
}
