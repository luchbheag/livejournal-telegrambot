package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.command.annotation.AdminCommand;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.UnparsedBlogService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.getChatId;

/**
 * Admin List Unparsed Blogs {@link Command}.
 */
@AdminCommand
public class AdminListCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UnparsedBlogService unparsedBlogService;

    public AdminListCommand(SendBotMessageService sendBotMessageService,
                            UnparsedBlogService unparsedBlogService) {
        this.sendBotMessageService = sendBotMessageService;
        this.unparsedBlogService = unparsedBlogService;
    }

    @Override
    public void execute(Update update) {
        List<UnparsedBlog> unparsedBlogs = unparsedBlogService.findAll();
        String chatId = getChatId(update);

        String message;
        if (unparsedBlogs.isEmpty()) {
            message = "There are no blogs waiting to be parsed.";
        } else {
            // TODO: here can be no users (users() emtpty)?
            message = "All blogs that are waiting to be parsed:\n\n" +
                    unparsedBlogs.stream().sorted(UnparsedBlog::compareTo)
                            .map(it -> String.format("%d users: %s (https://%s.livejournal.com)\n",
                                    it.getUsers().size(), it.getId(), it.getId()))
                            .collect(Collectors.joining());
        }
        sendBotMessageService.sendMessage(chatId, message);
    }
}
