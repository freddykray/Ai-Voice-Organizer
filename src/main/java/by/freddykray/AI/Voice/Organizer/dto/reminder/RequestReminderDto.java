package by.freddykray.AI.Voice.Organizer.dto.reminder;

import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import lombok.Data;

import java.time.Instant;

@Data
public class RequestReminderDto {

    private ParsedCommandType type;
    private String title;
    private String description;
    private Instant deadline;
    private boolean isHasExactTime;
}
