package by.freddykray.AI.Voice.Organizer.model.task;

import lombok.Data;

import java.time.Instant;

@Data
public class Task {

    private long id;

    private long userId;

    private String title;

    private Instant deadline;

    private boolean hasExactTime;

    private TaskStatus status;

    private Instant remindAt;

    private boolean reminderSent;

    private Instant createdAt;


}
