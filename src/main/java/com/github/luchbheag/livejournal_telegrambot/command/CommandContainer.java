package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.command.annotation.AdminCommand;
import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.*;

/**
 * Container of the {@link Command}'s, which are using for handling telegram commands.
 */
public class CommandContainer {
    private final ImmutableMap<String, Command> commandMap;
    private final Command unknownCommand;
    private final List<String> admins;

    public CommandContainer(SendBotMessageService sendBotMessageService,
                            TelegramUserService telegramUserService,
                            BlogSubService blogSubService,
                            LivejournalParser livejournalParser,
                            List<String> admins) {
        this.admins = admins;
        commandMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, telegramUserService))
                .put(ADD_BLOG_SUB.getCommandName(), new AddBlogSubCommand(sendBotMessageService, livejournalParser, blogSubService))
                .put(LIST_BLOG_SUB.getCommandName(), new ListBlogSubCommand(sendBotMessageService, telegramUserService))
                .put(DELETE_BLOG_SUB.getCommandName(), new DeleteBlogSubCommand(sendBotMessageService, telegramUserService, blogSubService))
                .put(ADMIN_HELP.getCommandName(), new AdminHelpCommand(sendBotMessageService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier, String username) {
        Command orDefault = commandMap.getOrDefault(commandIdentifier, unknownCommand);
        if (isAdminCommand(orDefault)) {
            if (admins.contains(username)) {
                return orDefault;
            } else {
                return unknownCommand;
            }
        }
        return commandMap.getOrDefault(commandIdentifier, unknownCommand);
    }

    private boolean isAdminCommand(Command command) {
        return command.getClass().getAnnotation(AdminCommand.class) != null;
    }
}
