package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.service.ConfirmationInfoService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.*;
import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.getChatId;
/**
 * Help {@link Command}.
 */
public class HelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("Available commands:\n\n"
            + "<b>Start/end working with bot</b>\n"
            + "%s - start working with bot\n"
            + "%s - end working with bot\n\n"
            + "<b>Work with subscription</b>\n"
            + "%s - subscribe on blog\n"
            + "%s - get list of all your subscriptions\n"
            + "%s - delete your subscription\n\n"
            + "%s - get help\n",
            START.getCommandName(), STOP.getCommandName(),
            ADD_BLOG_SUB.getCommandName(), LIST_BLOG_SUB.getCommandName(),
            DELETE_BLOG_SUB.getCommandName(),
            HELP.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(getChatId(update), HELP_MESSAGE);
    }
}
