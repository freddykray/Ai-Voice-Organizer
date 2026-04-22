package by.freddykray.AI.Voice.Organizer.telegrambot.config;

import by.freddykray.AI.Voice.Organizer.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class BotInitializer {

    @Bean
    public TelegramClient telegramClient(@Value("${telegram.bot.token}") String token) {
        return new OkHttpTelegramClient(token);
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
