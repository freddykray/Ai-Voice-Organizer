package by.freddykray.AI.Voice.Organizer.telegrambot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Component
public class BotInitializer {

    @Bean
    public TelegramBot myTelegramBot(@Value("${telegram.bot.token}") String botToken) {
        return new TelegramBot(botToken);
    }

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsApplication(
            @Value("${telegram.bot.token}") String botToken,
            TelegramBot myTelegramBot
    ) throws Exception {
        TelegramBotsLongPollingApplication application =
                new TelegramBotsLongPollingApplication();

        application.registerBot(botToken, myTelegramBot);
        return application;
    }
}
