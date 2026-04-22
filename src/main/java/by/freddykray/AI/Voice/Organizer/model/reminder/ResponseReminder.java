package by.freddykray.AI.Voice.Organizer.model.reminder;

import lombok.Data;

import java.time.Instant;

@Data
public class ResponseReminder {

    private long id;
    private String title;
    private String description;
    private boolean isHasExactTime;
    private Instant deadline;
}
