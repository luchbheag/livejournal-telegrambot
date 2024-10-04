package com.github.luchbheag.livejournal_telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {
		"com.github.luchbheag.livejournal_telegrambot",
		"org.telegram.telegrambots"
})
public class LiveJournalTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveJournalTelegramBotApplication.class, args);
	}

}
