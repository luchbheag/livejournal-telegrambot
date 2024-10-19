package com.github.luchbheag.livejournal_telegrambot.command.utils;

import com.github.luchbheag.livejournal_telegrambot.command.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Utils class for {@link Command}
 */
public class CommandUtils {

    /**
     * Get chatId from {@link Update} object.
     * @param {@link Update} update
     * @return {@link String} chatId
     */
    public static String getChatId(Update update) {
        return update.getMessage().getChatId().toString();
    }

    /**
     * Get message from {@link Update} object.
     * @param {@link Update} update
     * @return {@link String} text of message
     */
    public static String getMessage(Update update) {
        return update.getMessage().getText().trim();
    }

    public static String getUsername(Update update) {
        return update.getMessage().getFrom().getUserName();
    }
}
