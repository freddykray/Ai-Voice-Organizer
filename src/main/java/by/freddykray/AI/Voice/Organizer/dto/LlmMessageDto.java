package by.freddykray.AI.Voice.Organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LlmMessageDto {
    private String role;
    private String content;
}
