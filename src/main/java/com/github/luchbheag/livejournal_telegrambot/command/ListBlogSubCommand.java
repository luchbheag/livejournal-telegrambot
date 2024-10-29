package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import com.github.luchbheag.livejournal_telegrambot.service.UnparsedBlogService;
import jakarta.ws.rs.NotFoundException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.getChatId;

public class ListBlogSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public ListBlogSubCommand(SendBotMessageService sendBotMessageService,
                              TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        // TODO: exception handling
        TelegramUser telegramUser = telegramUserService.findByChatId(getChatId(update))
                .orElseThrow(NotFoundException::new);

        String message = "";

        List<BlogSub> blogSubs = telegramUser.getBlogSubs();
        List<UnparsedBlog> unparsedBlogs = telegramUser.getUnparsedBlogs();
        if (!containsBlogs(blogSubs) && !containsBlogs(unparsedBlogs)) {
            message = "You don't have any blog subscriptions yet!";
        } else {
            if (containsBlogs(blogSubs)) {
                message = "<b>All your blog subscriptions</b>:\n\n"
                        + blogSubs.stream()
                        .map(it -> String.format("Blog: %s (https://%s.livejournal.com)\n", it.getId(), it.getId()))
                        .collect(Collectors.joining());
            }
            if (containsBlogs(unparsedBlogs)) {
                if (!message.isEmpty()) {
                    message += "\n";
                }
                message += "<b>You are waiting for these blogs</b>:\n\n"
                        + unparsedBlogs.stream()
                        .map(it -> String.format("Blog: %s (https://%s.livejournal.com)\n", it.getId(), it.getId()))
                        .collect(Collectors.joining());
            }
        }
        sendBotMessageService.sendMessage(telegramUser.getChatId(), message);
    }

    private boolean containsBlogs(List<? extends Object> list) {
        return list != null && !list.isEmpty();
    }
}
