package com.github.luchbheag.livejournal_telegrambot.parser.excpection;

import java.io.IOException;

public class CannotParsePageException extends IOException {
    public CannotParsePageException(String message) {
        super(message);
    }
}
