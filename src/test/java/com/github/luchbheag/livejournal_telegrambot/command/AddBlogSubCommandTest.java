package com.github.luchbheag.livejournal_telegrambot.command;


import com.github.luchbheag.livejournal_telegrambot.parser.excpection.CannotParsePageException;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubService;
import com.github.luchbheag.livejournal_telegrambot.service.ConfirmationInfoService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import jakarta.ws.rs.NotFoundException;
import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.luchbheag.livejournal_telegrambot.command.AbstractCommandTest.prepareUpdate;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.ADD_BLOG_SUB;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.HELP;

@DisplayName("Unit-level testing for AddBlogSubCommand")
public class AddBlogSubCommandTest {

    private Command command;
    private SendBotMessageService sendBotMessageService;
    private BlogSubService blogSubService;
    private ConfirmationInfoService confirmationInfoService;

    @BeforeEach
    public void  init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        blogSubService = Mockito.mock(BlogSubService.class);
        confirmationInfoService = Mockito.mock(ConfirmationInfoService.class);
        command = new AddBlogSubCommand(sendBotMessageService,
                blogSubService, confirmationInfoService);
    }

    @Test
    public void shouldProperlyReturnMessageWhenNoSecondArgument() {
        // given
        Long chatId = 123456L;
        Update update = prepareUpdate(chatId, ADD_BLOG_SUB.getCommandName());
        String blogExampleMessage = "You should put name of the livejournal blog after command. For example,\n\n"
                + "/addblogsub example,\n\n"
                + "where example is name of the blog with address https://example.livejournal.com.";

        // when
        command.execute(update);

        // then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), blogExampleMessage);
    }

    @Test
    public void shouldProperlySendMessageIfNoBlogFound() throws HttpStatusException, NotFoundException, CannotParsePageException {
        // given
        Long chatId = 123456L;
        String blogName = "example";
        Update update = prepareUpdate(chatId, String.format("%s %s", ADD_BLOG_SUB.getCommandName(), blogName));
        Mockito.when(blogSubService.save(chatId.toString(), blogName)).thenThrow(NotFoundException.class);
        String blogNotFoundMessage = String.format("There is no blog with name = \"%s\"", blogName);

        // when
        command.execute(update);

        // then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), blogNotFoundMessage);
    }

    @Test
    public void shouldProperlySaveConfirmationInfoWhenUnparsedBlog() throws HttpStatusException, NotFoundException, CannotParsePageException {
        // given
        Long chatId = 123456L;
        String blogName = "example";
        Update update = prepareUpdate(chatId, String.format("%s %s", ADD_BLOG_SUB.getCommandName(), blogName));
        Mockito.when(blogSubService.save(chatId.toString(), blogName)).thenThrow(CannotParsePageException.class);
        String blogNotFoundMessage = String.format("I cannot parse the blog %s (https://%s.livejournal.com) for now. I can put you in the waiting list for it. "
                + "It means I'll work on this problem and send you notification after finishing. To confirm, write <b>yes</b>.\n\n"
                + "If you're not interested, just send me another command. To see all available commands, type \"%s\"", blogName, blogName, HELP.getCommandName());

        // when
        command.execute(update);

        // then
        Mockito.verify(confirmationInfoService).save(String.valueOf(chatId), blogName);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), blogNotFoundMessage);
    }

    @Test
    public void shouldProperlySaveSubCommandAndSendMessage() throws HttpStatusException, NotFoundException, CannotParsePageException {
        // given
        Long chatId = 123456L;
        String blogName = "example";
        Update update = prepareUpdate(chatId, String.format("%s %s", ADD_BLOG_SUB.getCommandName(), blogName));
        BlogSub blogSub = new BlogSub();
        blogSub.setId(blogName);
        Mockito.when(blogSubService.save(chatId.toString(), blogName)).thenReturn(blogSub);
        String message = String.format("I've subscribed you to blog %s", blogName);

        // when
        command.execute(update);

        // then
        Mockito.verify(blogSubService).save(String.valueOf(chatId), blogName);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), message);
    }
}
