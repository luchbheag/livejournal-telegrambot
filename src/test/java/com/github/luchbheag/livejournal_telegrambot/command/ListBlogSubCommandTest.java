package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import com.github.luchbheag.livejournal_telegrambot.service.UnparsedBlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.LIST_BLOG_SUB;
import static com.github.luchbheag.livejournal_telegrambot.command.AbstractCommandTest.prepareUpdate;

@DisplayName("Unit-level testing for ListGroupSubCommand")
public class ListBlogSubCommandTest {
    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;
    private Command command;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);
        command = new ListBlogSubCommand(sendBotMessageService, telegramUserService);
    }

    @Test
    public void shouldProperlySendMessageIsNoBlogsYet() {
        // given
        Long chatId = 123456L;
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(chatId));
        telegramUser.setActive(true);
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(telegramUser));
        Update update = prepareUpdate(chatId, LIST_BLOG_SUB.getCommandName());
        String expectedMessage = "You don't have any blog subscriptions yet!";

        // when
        command.execute(update);

        // then
        Mockito.verify(telegramUserService).findByChatId(String.valueOf(chatId));
        Mockito.verify(sendBotMessageService).sendMessage(String.valueOf(chatId), expectedMessage);
    }

    @Test
    public void shouldProperlyShowsListBlogSub() {
        // given
        Long chatId = 123456L;
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(chatId));
        telegramUser.setActive(true);

        List<BlogSub> blogSubList = new ArrayList<>();
        blogSubList.add(populateBlogSub("gs1"));
        blogSubList.add(populateBlogSub("gs2"));
        blogSubList.add(populateBlogSub("gs3"));
        blogSubList.add(populateBlogSub("gs4"));

        telegramUser.setBlogSubs(blogSubList);

        Mockito.when(telegramUserService.findByChatId(telegramUser.getChatId()))
                .thenReturn(Optional.of(telegramUser));

        Update update = prepareUpdate(chatId, LIST_BLOG_SUB.getCommandName());

        String collectedBlogs = "<b>All your blog subscriptions</b>:\n\n" +
                telegramUser.getBlogSubs().stream()
                        .map(it -> String.format("Blog: %s (https://%s.livejournal.com)\n", it.getId(), it.getId()))
                        .collect(Collectors.joining());

        // when
        command.execute(update);

        // then
        Mockito.verify(sendBotMessageService).sendMessage(telegramUser.getChatId(), collectedBlogs);
    }

    @Test
    public void shouldProperlyShowWaitingList() {
        // given
        Long chatId = 123456L;
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(chatId));
        telegramUser.setActive(true);

        List<UnparsedBlog> unparsedBlogList = new ArrayList<>();
        unparsedBlogList.add(populateUnparsedBlog("gs1"));
        unparsedBlogList.add(populateUnparsedBlog("gs2"));
        unparsedBlogList.add(populateUnparsedBlog("gs3"));
        unparsedBlogList.add(populateUnparsedBlog("gs4"));

        telegramUser.setUnparsedBlogs(unparsedBlogList);

        Mockito.when(telegramUserService.findByChatId(telegramUser.getChatId()))
                .thenReturn(Optional.of(telegramUser));

        Update update = prepareUpdate(chatId, LIST_BLOG_SUB.getCommandName());

        String collectedBlogs = "<b>You are waiting for these blogs</b>:\n\n" +
                telegramUser.getUnparsedBlogs().stream()
                        .map(it -> String.format("Blog: %s (https://%s.livejournal.com)\n", it.getId(), it.getId()))
                        .collect(Collectors.joining());

        // when
        command.execute(update);

        // then
        Mockito.verify(sendBotMessageService).sendMessage(telegramUser.getChatId(), collectedBlogs);

    }

    private BlogSub populateBlogSub(String id) {
        BlogSub blogSub = new BlogSub();
        blogSub.setId(id);
        return blogSub;
    }

    private UnparsedBlog populateUnparsedBlog(String id) {
        UnparsedBlog unparsedBlog = new UnparsedBlog();
        unparsedBlog.setId(id);
        return unparsedBlog;
    }
}
