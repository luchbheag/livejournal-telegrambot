package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.command.annotation.AdminCommand;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Statistics {@link Command}.
 */
@AdminCommand
public class StatCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public final static String STAT_MESSAGE = "This Telegram bot is used by %s users.";

    public StatCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        int activeUserCount = telegramUserService.retrieveAllActiveUsers().size();
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(),
                String.format(STAT_MESSAGE, activeUserCount));
    }
}
