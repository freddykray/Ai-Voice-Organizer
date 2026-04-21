package by.freddykray.AI.Voice.Organizer.dto.task;


import lombok.Data;

import java.time.Instant;

@Data
public class ParsedDeadlineDto {

    private Instant deadline;
    private boolean hasExactTime;
}
