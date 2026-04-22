package by.freddykray.AI.Voice.Organizer.service.idea;

import by.freddykray.AI.Voice.Organizer.model.idea.RequestIdea;
import by.freddykray.AI.Voice.Organizer.model.idea.ResponseIdea;
import by.freddykray.AI.Voice.Organizer.repository.IdeaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;

    public String create(RequestIdea request) {
        ideaRepository.create(request);

        if (request.getDescription() != null) {
            return "Идея: " + request.getTitle() + "\n" +
                    "Описание идеи: " + request.getDescription();
        } else {
            return "Идея: " + request.getTitle();
        }
    }

    public List<ResponseIdea> getAllIdeaUser(long chatId) {
        return ideaRepository.getAllIdeaUser(chatId);
    }

    public void delete(long ideaId) {
        ideaRepository.delete(ideaId);
    }
}
