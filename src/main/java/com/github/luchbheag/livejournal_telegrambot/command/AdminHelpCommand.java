package com.github.luchbheag.livejournal_telegrambot.command;

import com.github.luchbheag.livejournal_telegrambot.service.ConfirmationInfoService;
import com.github.luchbheag.livejournal_telegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import static com.github.luchbheag.livejournal_telegrambot.command.CommandName.STAT;
import static com.github.luchbheag.livejournal_telegrambot.command.utils.CommandUtils.getChatId;
/**
 * Admin Help {@link Command}.
 */
public class AdminHelpCommand implements Command {
    public static final String ADMIN_HELP_MESSAGE = String.format(
            "<b>Available admin commands:\n\n"
            + "%s - bot stats\n",
            STAT.getCommandName());
    private final SendBotMessageService sendBotMessageService;
    private final ConfirmationInfoService confirmationInfoService;

    public AdminHelpCommand(SendBotMessageService sendBotMessageService,
                            ConfirmationInfoService confirmationInfoService) {
        this.sendBotMessageService = sendBotMessageService;
        this.confirmationInfoService = confirmationInfoService;
    }

    @Override
    public void execute(Update update) {
        String chatId = getChatId(update);
        confirmationInfoService.deleteById(chatId);
        sendBotMessageService.sendMessage(chatId, ADMIN_HELP_MESSAGE);
    }
}
