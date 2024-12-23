package com.github.luchbheag.livejournal_telegrambot.command;

/**
 * Enumeration for {@Link Command}'s.
 */
public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    STAT("/stat"),
    NO("nocommand"),
    ADD_BLOG_SUB("/addblogsub"),
    LIST_BLOG_SUB("/listblogsub"),
    DELETE_BLOG_SUB("/deleteblogsub"),
    CONFIRM("confirmcommand"),
    ADMIN_HELP("/ahelp"),
    ADMIN_LIST("/alist");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
