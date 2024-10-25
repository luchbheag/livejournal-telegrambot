package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;
import com.github.luchbheag.livejournal_telegrambot.service.*;
import jakarta.ws.rs.NotFoundException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.*;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.DELETE_BLOG_SUB;
import static org.apache.commons.lang3.StringUtils.SPACE;
/**
 * Delete Group subscription {@link Command}.
 */
public class DeleteBlogSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final BlogSubService blogSubService;
    private final UnparsedBlogService unparsedBlogService;

    public DeleteBlogSubCommand(SendBotMessageService sendBotMessageService,
                                TelegramUserService telegramUserService,
                                BlogSubService blogSubService,
                                UnparsedBlogService unparsedBlogService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.blogSubService = blogSubService;
        this.unparsedBlogService = unparsedBlogService;
    }

    @Override
    public void execute(Update update) {
        final String SUCCESS_MESSAGE = "I've deleted your subscription on blog: %s.";
        final String SUCCESS_UNPARSED_BLOG_MESSAGE = "I've deleted you from a waiting list of blog: %s.";
        final String NOT_FOUND_MESSAGE = "I haven't found subscription %s.";
        boolean wasSubscribed = false;
        boolean wasWaiting = false;

        String chatId = getChatId(update);
        if (getMessage(update).equalsIgnoreCase(DELETE_BLOG_SUB.getCommandName())) {
            sendBlogList(chatId);
        } else {
            String blogId = getMessage(update).split(SPACE)[1];
            Optional<BlogSub> optionalBlogSub = blogSubService.findById(blogId);
            TelegramUser telegramUser = telegramUserService.findByChatId(chatId)
                    .orElseThrow(NotFoundException::new);
            // TODO: too many methods, split into private (and probably unite some parts)
            if (optionalBlogSub.isPresent()) {
                BlogSub blogSub = optionalBlogSub.get();
                wasSubscribed = blogSub.getUsers().remove(telegramUser);
                if (wasSubscribed) {
                    if (blogSub.getUsers().isEmpty()) {
                        blogSubService.delete(blogId);
                    } else {
                        blogSubService.save(blogSub);
                    }
                }
            } else {
                Optional<UnparsedBlog> optionalUnparsedBlog = unparsedBlogService.findById(blogId);
                if (optionalUnparsedBlog.isPresent()) {
                    UnparsedBlog unparsedBlog = optionalUnparsedBlog.get();
                    wasWaiting = unparsedBlog.getUsers().remove(telegramUser);
                    if (wasWaiting) {
                        if (unparsedBlog.getUsers().isEmpty()) {
                            unparsedBlogService.delete(blogId);
                        } else {
                            unparsedBlogService.save(unparsedBlog);
                        }
                    }
                }
            }
            if (wasSubscribed) {
                sendBotMessageService.sendMessage(chatId, String.format(SUCCESS_MESSAGE, blogId));
            } else if (wasWaiting) {
                sendBotMessageService.sendMessage(chatId, String.format(SUCCESS_UNPARSED_BLOG_MESSAGE, blogId));
            } else {
                sendBotMessageService.sendMessage(chatId, String.format(NOT_FOUND_MESSAGE, blogId));
            }
        }
    }

    private void sendBlogList(String chatId) {
        String message;
        List<BlogSub> blogSubs = telegramUserService.findByChatId(chatId)
                .orElseThrow(NotFoundException::new)
                .getBlogSubs();
        if (blogSubs.isEmpty()) {
            message = "You don't have any blog subscriptions yet. To add one, write /addblogsub";
        } else {
            message = "To delete blog subscription, send command with blog name. \n"
                    + "For example, /deleteblogsub example \n\n"
                    + "Here is all your subscriptions:\n\n"
                    + "%s";
        }

        String userBlogSubData = blogSubs.stream()
                .map(blog -> String.format("%s (https://%s.livejournal.com)\n", blog.getId(), blog.getId()))
                .collect(Collectors.joining());

        sendBotMessageService.sendMessage(chatId, String.format(message, userBlogSubData));
    }
}
