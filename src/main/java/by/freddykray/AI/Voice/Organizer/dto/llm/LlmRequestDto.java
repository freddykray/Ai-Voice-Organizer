package by.freddykray.AI.Voice.Organizer.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LlmRequestDto {

    private String model;
    private List<LlmMessageDto> messages;
    private Double temperature;
    private Integer max_tokens;

}
