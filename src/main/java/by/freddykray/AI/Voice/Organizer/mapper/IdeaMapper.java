package by.freddykray.AI.Voice.Organizer.mapper;

import by.freddykray.AI.Voice.Organizer.dto.idea.RequestIdeaDto;
import by.freddykray.AI.Voice.Organizer.model.idea.RequestIdea;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IdeaMapper {

    RequestIdea toModelRequestIdea(long chatId, RequestIdeaDto dto);
}
