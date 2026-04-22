package by.freddykray.AI.Voice.Organizer.telegrambot.handler;

import by.freddykray.AI.Voice.Organizer.service.telegramuser.TelegramUserService;
import by.freddykray.AI.Voice.Organizer.telegrambot.keyboard.KeyboardFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartCommandHandler implements BotTextHandler {

    private final MessageSender messageSender;
    private final KeyboardFactory keyboardFactory;
    private final TelegramUserService telegramUserService;

    @Override
    public boolean canHandle(String text) {
        return "/start".equals(text);
    }

    @Override
    public void handle(long chatId, String text) {
        String response = """
                Привет! 👋
                
                Спасибо, что заглянул в моего бота. 🥳
                
                Немного правил:
                1. Не ругайся на него — он ещё учится.
                2. Если он что-то просит уточнить или сделать, не вредничай — просто спокойно ответь. 🤖
                3. Если что-то сломалось или бот работает неправильно, напиши мне в личку: @freddykray.
                4. Основное общение с ботом происходит голосом, но можно и текстом. Поэтому старайся говорить громко, чётко и без лишних помех.
                
                Теперь немного о самом боте.
                
                Его зовут Митяй. Его главная мечта — помогать людям.
                
                Сейчас он умеет:
                1. Создавать задачи с дедлайном.
                   Тут есть важный момент: если указать дедлайн просто как «завтра», напоминание о задаче не сработает.
                   Если напоминание нужно, лучше сказать точную дату и время, например: «завтра в 3 часа дня».
                   Если дату не указать совсем, Митяй попросит тебя её уточнить.
                   Если срок задачи истечёт, Митяй сообщит об этом.
                   Пример: Создай задачу на завтра в 3 часа дня, покормить кошку
                
                2. Создавать напоминания.
                   Здесь всё работает почти так же, как и с задачами.
                    Пример: Напомни мне завтра в 3 часа дня покормить кошку
                
                3. Сохранять идеи.
                   Тут всё просто: если тебе пришла мысль, просто расскажи её Митяю своими словами.
                   Он всё аккуратно сохранит и упорядочит.
                   Главное — говори громче и чётче, а то он немного туговат на одно ухо.
                Пример: Придумал интересную идею... (главное что бы было слово идея)
                
                В настройках можно указать, за сколько времени до дедлайна Митяй должен предупредить тебя о том, что срок задачи скоро истечёт (сейчас стоит за 3 часа).
                """;

        telegramUserService.ensureUserExists(chatId);

        messageSender.send(
                keyboardFactory.createMainMenu(chatId, response)
        );
    }
}