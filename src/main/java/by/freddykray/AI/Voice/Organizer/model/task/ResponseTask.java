package by.freddykray.AI.Voice.Organizer.model.task;

import lombok.Data;

import java.time.Instant;

@Data
public class ResponseTask {
    private long id;
    private String title;
    private Instant deadline;
    private boolean hasExactTime;
}
