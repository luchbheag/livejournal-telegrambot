package com.github.luchbheag.livejournal_telegrambot.service;

import java.util.List;

/**
 * Service for sending messages via telegram bot.
 */
public interface SendBotMessageService {

    /**
     * Send message via telegram bot.
     *
     * @param chatId provided chatId in which messages would be sent.
     * @param message provided message to be sent.
     */
    void sendMessage(String chatId, String message);

    /**
     * Send collection of messages via telegram bot.
     *
     * @param chatId provided chatId in which messages would be sent.
     * @param messages provided messages to be sent.
     */
    void sendMessages(String chatId, List<String> messages);
}
