package by.freddykray.AI.Voice.Organizer.dto.task;

import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import lombok.Data;

import java.time.Instant;

@Data
public class RequestTaskDto {

    private ParsedCommandType type;
    private String title;
    private Instant deadline;
    private boolean hasExactTime;
}
