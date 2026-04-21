package by.freddykray.AI.Voice.Organizer.mapper;

import by.freddykray.AI.Voice.Organizer.dto.task.ParsedDeadlineDto;
import by.freddykray.AI.Voice.Organizer.dto.task.RequestTaskDto;
import by.freddykray.AI.Voice.Organizer.model.task.RequestTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    RequestTask toModelRequestTask(long chatId, int remindBefore, RequestTaskDto dto);

    @Mapping(target = "chatId", source = "chatId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "deadline", source = "deadlineDto.deadline")
    @Mapping(target = "hasExactTime", source = "deadlineDto.hasExactTime")
    RequestTask toRequestTask(long chatId, String title, ParsedDeadlineDto deadlineDto);}
