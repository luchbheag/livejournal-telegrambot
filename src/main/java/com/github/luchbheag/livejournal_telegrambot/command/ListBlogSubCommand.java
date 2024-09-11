package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import com.github.luchbheag.livejournal_telegrambot.service.TelegramUserService;
import jakarta.ws.rs.NotFoundException;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.getChatId;

public class ListBlogSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public ListBlogSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        // TODO: exception handling
        TelegramUser telegramUser = telegramUserService.findByChatId(getChatId(update))
                .orElseThrow(NotFoundException::new);

        String message;

        List< BlogSub> blogSubs = telegramUser.getBlogSubs();
        if (blogSubs.isEmpty()) {
            message = "You don't have any blog subscriptions yet!";
        } else {
            message = "I've found all your blog subscriptions:\n\n"
                    + blogSubs.stream()
                    .map(it -> String.format("Blog: %s (https://%s.livejournal.com)\n", it.getId(), it.getId()))
                    .collect(Collectors.joining());
        }

        sendBotMessageService.sendMessage(telegramUser.getChatId(), message);
    }
}
