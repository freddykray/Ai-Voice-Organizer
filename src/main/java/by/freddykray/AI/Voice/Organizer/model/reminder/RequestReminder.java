package by.freddykray.AI.Voice.Organizer.model.reminder;

import lombok.Data;

import java.time.Instant;

@Data
public class RequestReminder {

    private long chatId;
    private String title;
    private String description;
    private Instant remindAt;
    private boolean isHasExactTime;
    private int remindBefore;
    private Instant deadline;
}
