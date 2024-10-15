package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ConfirmationInfo;
import com.github.luchbheag.livejournal_telegrambot.service.ConfirmationInfoService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.UnparsedBlogService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.*;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.ADD_BLOG_SUB;

/**
 * Confirm {@link Command} for putting unparsed blog in a waiting list.
 */
public class ConfirmCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final UnparsedBlogService unparsedBlogService;
    private final ConfirmationInfoService confirmationInfoService;

    public ConfirmCommand(SendBotMessageService sendBotMessageService,
                          UnparsedBlogService unparsedBlogService,
                          ConfirmationInfoService confirmationInfoService) {
        this.sendBotMessageService = sendBotMessageService;
        this.unparsedBlogService = unparsedBlogService;
        this.confirmationInfoService = confirmationInfoService;
    }

    @Override
    public void execute(Update update) {
        String chatId = getChatId(update);
        Optional<ConfirmationInfo> confirmationInfoFromDB = confirmationInfoService.findById(chatId);
        String message;
        if (confirmationInfoFromDB.isPresent()) {
            String blogName = confirmationInfoFromDB.get().getBlogName();
            unparsedBlogService.save(chatId, blogName);
            confirmationInfoService.delete(confirmationInfoFromDB.get());
            message = String.format("I've added you to the waiting list for blog %s (https://%s.livejournal.com). "
                    + "You'll get a notification, when the blog will be available for subscription.",
                    blogName, blogName);
        } else {
            // TODO: add logging here and probably throw exception?
            message = String.format("Something went wrong. Please, send %s command again.", ADD_BLOG_SUB.getCommandName());
        }
        sendBotMessageService.sendMessage(chatId, message);
    }
}
