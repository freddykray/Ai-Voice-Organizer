package by.freddykray.AI.Voice.Organizer.service;

import by.freddykray.AI.Voice.Organizer.repository.ReminderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
}
