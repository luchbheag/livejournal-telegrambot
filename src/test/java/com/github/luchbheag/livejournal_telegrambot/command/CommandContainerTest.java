package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import com.github.luchbheag.livejournal_telegrambot.service.UnparsedBlogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

@DisplayName("Unit-level testing for CommandContainer")
public class CommandContainerTest {

    private CommandContainer commandContainer;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        BlogSubService blosubService = Mockito.mock(BlogSubService.class);
        LivejournalParser livejournalParser = Mockito.mock(LivejournalParser.class);
        UnparsedBlogService unparsedBlogService = Mockito.mock(UnparsedBlogService.class);
        commandContainer = new CommandContainer(sendBotMessageService, telegramUserService,
                blosubService, livejournalParser, unparsedBlogService, List.of("username"));
    }

    @Test
    public void shouldGetAllTheExistingCommands() {
        // when-then
        Arrays.stream(CommandName.values()).forEach(commandName -> {
            Command command = commandContainer.retrieveCommand(commandName.getCommandName(), "username");
            Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
        });
    }

    @Test
    public void shouldReturnUnknownCommand() {
        // given
        String unknownCommand = "/asdkldanngw";

        // when
        Command command = commandContainer.retrieveCommand(unknownCommand, "username");

        // then
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }

}
