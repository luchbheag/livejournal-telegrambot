package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import com.github.luchbheag.livejournal_telegrambot.service.UnparsedBlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.LIST_BLOG_SUB;

@DisplayName("Unit-level testing for ListGroupSubCommand")
public class ListBlogSubCommandTest {
    private SendBotMessageService sendBotMessageService;
    private TelegramUserService telegramUserService;
    private UnparsedBlogService unparsedBlogService;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        UnparsedBlogService unparsedBlogService = Mockito.mock(UnparsedBlogService.class);
    }

    @Test
    public void shouldProperlyShowsListBlogSub() {
        // given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("1");
        telegramUser.setActive(true);

        List<BlogSub> blogSubList = new ArrayList<>();
        blogSubList.add(populateBlogSub("gs1"));
        blogSubList.add(populateBlogSub("gs2"));
        blogSubList.add(populateBlogSub("gs3"));
        blogSubList.add(populateBlogSub("gs4"));

        telegramUser.setBlogSubs(blogSubList);

        Mockito.when(telegramUserService.findByChatId(telegramUser.getChatId()))
                .thenReturn(Optional.of(telegramUser));

        ListBlogSubCommand command = new ListBlogSubCommand(sendBotMessageService, telegramUserService, unparsedBlogService);

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(Long.valueOf(telegramUser.getChatId()));
        Mockito.when(message.getText()).thenReturn(LIST_BLOG_SUB.getCommandName());
        update.setMessage(message);

        String collectedBlogs = "I've found all your blog subscriptions:\n\n" +
                telegramUser.getBlogSubs().stream()
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
}
