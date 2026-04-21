package by.freddykray.AI.Voice.Organizer.model.task;

import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import lombok.Data;

import java.time.Instant;

@Data
public class RequestTask {

    private long chatId;
    private ParsedCommandType type;
    private String title;
    private Instant deadline;
    private boolean hasExactTime;
    private int remindBefore;
    private Instant remindAt;
}
