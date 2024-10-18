package com.github.luchbheag.livejournal_telegrambot.bot;

import com.github.luchbheag.livejournal_telegrambot.command.CommandContainer;
import com.github.luchbheag.livejournal_telegrambot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.CONFIRM;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.NO;
import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.*;

/**
 * Telegram bot for checking new article in LiveJournal blogs.
 */
@Component
public class LiveJournalTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    private final ConfirmationInfoService confirmationInfoService;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private final CommandContainer commandContainer;

    @Autowired
    public LiveJournalTelegramBot(TelegramUserService telegramUserService,
                                  BlogSubService blogSubService,
                                  UnparsedBlogService unparsedBlogService,
                                  ConfirmationInfoService confirmationInfoService,
                                  @Value("#{'${bot.admins}'.split(',')}") List<String> admins) {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this),
                telegramUserService,
                blogSubService,
                unparsedBlogService,
                confirmationInfoService,
                admins);
        this.confirmationInfoService = confirmationInfoService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        final String YES = "yes";
        String chatUsername = getUsername(update);
        boolean isWaitingForConfirm = checkWaitingForConfirm(chatUsername);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = getMessage(update);
            if (message.startsWith(COMMAND_PREFIX)) {
                if (isWaitingForConfirm) {
                    confirmationInfoService.deleteById(chatUsername);
                }
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier, chatUsername).execute(update);
            } else if (isWaitingForConfirm) {
                if (message.equalsIgnoreCase(YES)) {
                    commandContainer.retrieveCommand(CONFIRM.getCommandName(), chatUsername).execute(update);
                } else {
                    // TODO: if waiting for confirm, recieve no command w/ other text
                    commandContainer.retrieveCommand(NO.getCommandName(), chatUsername).execute(update);
                }
            } else {
                commandContainer.retrieveCommand(NO.getCommandName(), chatUsername).execute(update);
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

    private boolean checkWaitingForConfirm(String chatId) {
        return confirmationInfoService.findById(chatId).isPresent();
    }
}
