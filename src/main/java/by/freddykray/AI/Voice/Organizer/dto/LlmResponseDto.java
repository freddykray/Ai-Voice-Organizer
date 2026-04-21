package by.freddykray.AI.Voice.Organizer.dto;

import lombok.Data;

import java.util.List;

@Data
public class LlmResponseDto {

    private List<LlmChoiceDto> choices;
}
