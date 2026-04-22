package by.freddykray.AI.Voice.Organizer.telegrambot.handler.callbackhandler;

import by.freddykray.AI.Voice.Organizer.service.idea.IdeaService;
import by.freddykray.AI.Voice.Organizer.telegrambot.handler.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteIdeaCallbackHandler implements BotCallbackHandler{

    private final IdeaService ideaService;
    private final MessageSender messageSender;

    @Override
    public boolean canHandle(String callbackData) {
        return callbackData != null && callbackData.startsWith("delete_idea:");
    }

    @Override
    public void handle(long chatId, String callbackData) {
        long ideaId = Long.parseLong(callbackData.substring("delete_idea:".length()));
        ideaService.delete(ideaId);
        messageSender.send(chatId, "Идея успешна удалена!");
    }
}
