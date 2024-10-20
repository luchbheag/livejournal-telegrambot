package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubService;
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

import static com.github.luchbheag.livejournal_telegrambot.command.AbstractCommandTest.prepareUpdate;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.DELETE_BLOG_SUB;

@DisplayName("Unit-level testing for DeleteBlogSubCommand")
public class DeleteBlogSubCommandTest {

    private Command command;
    private SendBotMessageService sendBotMessageService;
    BlogSubService blogSubService;
    TelegramUserService telegramUserService;
    UnparsedBlogService unparsedBlogService;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        blogSubService = Mockito.mock(BlogSubService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);
        unparsedBlogService = Mockito.mock(UnparsedBlogService.class);

        command = new DeleteBlogSubCommand(sendBotMessageService, telegramUserService,
                blogSubService, unparsedBlogService);
    }

    @Test
    public void shouldProperlyReturnEmptySubscriptionList() {
        // given
        Long chatId = 123456L;
        Update update = prepareUpdate(chatId, DELETE_BLOG_SUB.getCommandName());

        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId.toString());
        telegramUser.setBlogSubs(new ArrayList<>());
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId))).thenReturn(Optional.of(telegramUser));

        String expectedMessage = "You don't have any blog subscriptions yet. To add one, write /addblogsub";

        // when
        command.execute(update);

        // then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldProperlyReturnSubscriptionList() {
        // given
        Long chatId = 123456L;
        Update update = prepareUpdate(chatId, DELETE_BLOG_SUB.getCommandName());
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(chatId));
        String blogId = "example";
        BlogSub blogSub = new BlogSub();
        blogSub.setId(blogId);
        List<BlogSub> blogSubs = new ArrayList<>();
        blogSubs.add(blogSub);
        telegramUser.setBlogSubs(blogSubs);
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId))).thenReturn(Optional.of(telegramUser));

        String expectedMessage = String.format("To delete blog subscription, send command with blog name. \n"
                + "For example, /deleteblogsub example \n\n"
                + "Here is all your subscriptions:\n\n"
                + "%s (https://%s.livejournal.com)\n", blogId, blogId);

        // when
        command.execute(update);

        // then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldProperlyDeleteByBlogIdWhenOnlySubscriber() {
        // given
        Long chatId = 123456L;
        String blogId = "example";
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_BLOG_SUB.getCommandName(), blogId));

        BlogSub blogSub = new BlogSub();
        blogSub.setId(blogId);
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(chatId));
        telegramUser.setBlogSubs(List.of(blogSub));
        List<TelegramUser> users = new ArrayList<>();
        users.add(telegramUser);
        blogSub.setUsers(users);
        Mockito.when(blogSubService.findById(blogId)).thenReturn(Optional.of(blogSub));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId))).thenReturn(Optional.of(telegramUser));

        String expectedMessage = String.format("I've deleted your subscription on blog: %s.", blogId);

        // when
        command.execute(update);

        // then
        users.remove(telegramUser);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldProperlyDeleteByBlogIdWhenTwoSubscribers() {
        // given
        Long chatIdFirst = 123456L;
        Long chatIdSecond = 654321L;
        String blogId = "example";
        Update update = prepareUpdate(chatIdFirst, String.format("%s %s", DELETE_BLOG_SUB.getCommandName(), blogId));

        BlogSub blogSub = new BlogSub();
        blogSub.setId(blogId);
        TelegramUser telegramUserFirst = new TelegramUser();
        telegramUserFirst.setChatId(String.valueOf(chatIdFirst));
        telegramUserFirst.setBlogSubs(List.of(blogSub));
        TelegramUser telegramUserSecond = new TelegramUser();
        telegramUserSecond.setChatId(String.valueOf(chatIdSecond));
        telegramUserSecond.setBlogSubs(List.of(blogSub));
        List<TelegramUser> users = new ArrayList<>();
        users.add(telegramUserFirst);
        users.add(telegramUserSecond);
        blogSub.setUsers(users);
        Mockito.when(blogSubService.findById(blogId)).thenReturn(Optional.of(blogSub));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatIdFirst))).thenReturn(Optional.of(telegramUserFirst));

        String expectedMessage = String.format("I've deleted your subscription on blog: %s.", blogId);

        // when
        command.execute(update);

        // then
        users.remove(telegramUserFirst);
        Mockito.verify(blogSubService).save(blogSub);
        Mockito.verify(sendBotMessageService).sendMessage(chatIdFirst.toString(), expectedMessage);
    }

    @Test
    public void shouldNotifyThatDoesNotFindSubscriptionByBlogId() {
        // given
        Long chatId = 123456L;
        String blogId = "example";
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_BLOG_SUB.getCommandName(), blogId));

        Mockito.when(blogSubService.findById(blogId)).thenReturn(Optional.empty());

        String expectedMessage = String.format("I haven't found subscription %s.", blogId);

        // when
        command.execute(update);

        // then
        Mockito.verify(blogSubService).findById(blogId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }
}
