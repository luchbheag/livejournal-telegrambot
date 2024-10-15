package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.parser.excpection.CannotParsePageException;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.service.BlogSubService;
import com.github.luchbheag.livejournal_telegrambot.service.ConfirmationInfoService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import jakarta.ws.rs.NotFoundException;
import org.jsoup.HttpStatusException;
import org.telegram.telegrambots.meta.api.objects.Update;


import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.HELP;
import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.*;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.ADD_BLOG_SUB;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Add Blog subscription {@link Command}.
 */
public class AddBlogSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final BlogSubService blogSubService;
    private final ConfirmationInfoService confirmationInfoService;

    public AddBlogSubCommand(SendBotMessageService sendBotMessageService,
                             BlogSubService blogSubService,
                             ConfirmationInfoService confirmationInfoService) {
        this.sendBotMessageService = sendBotMessageService;
        this.blogSubService = blogSubService;
        this.confirmationInfoService = confirmationInfoService;
    }

    @Override
    public void execute(Update update) {
        String chatId = getChatId(update);
        confirmationInfoService.deleteById(chatId);
        if (getMessage(update).equalsIgnoreCase(ADD_BLOG_SUB.getCommandName())) {
            sendBlogExample(chatId);
            return;
        }
        String blogName = getMessage(update).split(SPACE)[1];

        try {
            BlogSub savedBlogSub = blogSubService.save(chatId, blogName);
            sendBotMessageService.sendMessage(chatId, "I've subscribed you to blog " + savedBlogSub.getId());
        } catch (CannotParsePageException e) {
            confirmationInfoService.save(chatId, blogName);
            sendAskForVerification(chatId, blogName);
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

    private void sendAskForVerification(String chatId, String blogName) {
        String message = "I cannot parse the blog %s (https://%s.livejournal.com) for now. I can put you in the waiting list for it. "
        + "It means I'll work on this problem and send you notification after finishing. To confirm, write <b>yes</b>.\n\n"
                + "If you're not interested, just send me another command. To see all available commands, type \"%s\"";
        sendBotMessageService.sendMessage(chatId, String.format(message, blogName, blogName, HELP.getCommandName()));
    }
}
