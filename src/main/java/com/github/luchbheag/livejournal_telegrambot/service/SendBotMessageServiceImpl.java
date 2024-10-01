package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.bot.LiveJournalTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * Implementation of {@link SendBotMessageService} interface.
 */
@Service
public class SendBotMessageServiceImpl implements SendBotMessageService {

    private final LiveJournalTelegramBot livejournalBot;

    @Autowired
    public SendBotMessageServiceImpl(LiveJournalTelegramBot livejournalBot) {
        this.livejournalBot = livejournalBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            livejournalBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            // TODO add logging to the project.
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessages(String chatId, List<String> messages) {
        if (messages.isEmpty()) {
            return;
        }
        messages.forEach(message -> sendMessage(chatId, message));
    }
}
