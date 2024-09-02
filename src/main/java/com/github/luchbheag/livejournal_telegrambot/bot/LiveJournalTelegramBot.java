package com.github.luchbheag.livejournal_telegrambot.bot;

import com.github.luchbheag.livejournal_telegrambot.command.CommandContainer;
import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParserImpl;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubService;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubServiceImpl;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageServiceImpl;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.NO;

/**
 * Telegram bot for checking new article in LiveJournal blogs.
 */
@Component
public class LiveJournalTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private final CommandContainer commandContainer;

    @Autowired
    public LiveJournalTelegramBot(TelegramUserService telegramUserService,
                                  BlogSubService blogSubService,
                                  LivejournalParser livejournalParser) {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this),
                telegramUserService,
                blogSubService,
                livejournalParser);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)) {
                String commandIdentifirer = message.split(" ")[0].toLowerCase();

                commandContainer.retrieveCommand(commandIdentifirer).execute(update);
            } else {
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
