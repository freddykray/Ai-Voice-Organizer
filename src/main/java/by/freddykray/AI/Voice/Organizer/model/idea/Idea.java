package by.freddykray.AI.Voice.Organizer.model.idea;

import lombok.Data;

import java.time.Instant;

@Data
public class Idea {

    private long chatId;
    private String title;
    private String description;
    private Instant createdAt;

}
