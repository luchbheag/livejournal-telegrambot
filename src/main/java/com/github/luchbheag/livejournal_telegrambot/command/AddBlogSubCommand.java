package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import jakarta.ws.rs.NotFoundException;
import org.jsoup.HttpStatusException;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.*;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.ADD_BLOG_SUB;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Add Blog subscription {@link Command}.
 */
public class AddBlogSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final LivejournalParser livejournalParser;
    private final BlogSubService blogSubService;

    public AddBlogSubCommand(SendBotMessageService sendBotMessageService,
                             LivejournalParser livejournalParser,
                             BlogSubService blogSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.livejournalParser = livejournalParser;
        this.blogSubService = blogSubService;
    }

    @Override
    public void execute(Update update) {
        String chatId = getChatId(update);
        if (getMessage(update).equalsIgnoreCase(ADD_BLOG_SUB.getCommandName())) {
            // should be like that /addblogsub
            sendBlogExample(chatId);
            return;
        }
        String blogName = getMessage(update).split(SPACE)[1];

        try {
            BlogSub savedBlogSub = blogSubService.save(chatId, blogName);
            sendBotMessageService.sendMessage(chatId, "I've subscribed you to blog " + savedBlogSub.getId());
        } catch (HttpStatusException | NotFoundException httpStatusException) {
            sendBlogNotFound(chatId, blogName);
        }
    }

    private void sendBlogNotFound(String chatId, String blogName) {
        String blogNotFoundMessage = "There is no blog with name = \"%s\"";
        sendBotMessageService.sendMessage(chatId, String.format(blogNotFoundMessage, blogName));
    }

    private void sendBlogExample(String chatId) {
        String blogExampleMessage = "You should put name of the livejournal blog after command. For example,\n\n"
                + "/addblogsub example,\n\n"
                + "where example is name of the blog with address https://example.livejournal.com.";

        sendBotMessageService.sendMessage(chatId, blogExampleMessage);
    }
}
