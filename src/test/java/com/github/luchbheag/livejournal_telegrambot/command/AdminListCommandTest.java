package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.UnparsedBlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.github.luchbheag.livejournal_telegrambot.command.AbstractCommandTest.prepareUpdate;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.ADMIN_LIST;


@DisplayName("Unit-level testing for AdminListUnparsedBlogsCommand")
public class AdminListCommandTest {

    private Command command;
    private SendBotMessageService sendBotMessageService;
    private UnparsedBlogService unparsedBlogService;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        unparsedBlogService = Mockito.mock(UnparsedBlogService.class);
        command = new AdminListCommand(sendBotMessageService, unparsedBlogService);
    }

    public void shouldProperlyReturnMessageIfThereIsNoUnparsedBlogs() {
        // given
        Long chatId = 123456L;
        Update update = prepareUpdate(chatId, ADMIN_LIST.getCommandName());
        String expectedMessage = "There are no blogs waiting to be parsed.";
        Mockito.when(unparsedBlogService.findAll()).thenReturn(new ArrayList<>());

        // when
        command.execute(update);

        // then
        Mockito.verify(unparsedBlogService).findAll();
        Mockito.verify(sendBotMessageService).sendMessage(String.valueOf(chatId), expectedMessage);
    }

    public void shouldProperlyDisplayUnparsedBlogsInCorrectOrder() {
        // given
        Long chatId = 123456L;
        Update update = prepareUpdate(chatId, ADMIN_LIST.getCommandName());
        TelegramUser firstUser = new TelegramUser();
        TelegramUser secondUser = new TelegramUser();
        List<UnparsedBlog> unparsedBlogs = new ArrayList<>();

        UnparsedBlog firstUnparsedBlog = new UnparsedBlog();
        firstUnparsedBlog.setId("example_a");
        firstUnparsedBlog.addUser(firstUser);
        unparsedBlogs.add(firstUnparsedBlog);

        UnparsedBlog secondUnparsedBlog = new UnparsedBlog();
        secondUnparsedBlog.setId("example_b");
        secondUnparsedBlog.addUser(firstUser);
        firstUnparsedBlog.addUser(secondUser);
        unparsedBlogs.add(secondUnparsedBlog);
        Mockito.when(unparsedBlogService.findAll()).thenReturn(unparsedBlogs);

        String expectedMessage = String.format("All blogs that are waiting to be parsed:\n\n"
                + "%d users: %s (https://%s.livejournal.com)\n"
                + "%d users: %s (https://%s.livejournal.com)\n",
                2, secondUnparsedBlog.getId(), secondUnparsedBlog.getId(),
                1, firstUnparsedBlog.getId(), firstUnparsedBlog.getId());

        // when
        command.execute(update);

        // then
        Mockito.verify(unparsedBlogService).findAll();
        Mockito.verify(sendBotMessageService).sendMessage(String.valueOf(chatId), expectedMessage);
    }

}
