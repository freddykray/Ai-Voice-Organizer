package by.freddykray.AI.Voice.Organizer.dto.idea;

import by.freddykray.AI.Voice.Organizer.model.ParsedCommandType;
import lombok.Data;

@Data
public class RequestIdeaDto {
    private ParsedCommandType type;
    private String title;
    private String description;
}
