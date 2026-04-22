package by.freddykray.AI.Voice.Organizer.mapper;


import by.freddykray.AI.Voice.Organizer.dto.reminder.RequestReminderDto;
import by.freddykray.AI.Voice.Organizer.model.reminder.RequestReminder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReminderMapper {

    RequestReminder toModelRequestReminder(long chatId, int remindBefore, RequestReminderDto dto);
}
