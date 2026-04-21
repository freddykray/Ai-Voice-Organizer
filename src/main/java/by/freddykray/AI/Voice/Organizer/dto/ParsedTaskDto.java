package by.freddykray.AI.Voice.Organizer.dto;

import lombok.Data;

@Data
public class ParsedTaskDto {

    private ParsedCommandType type;
    private String title;
    private String deadline;
}
