package com.github.luchbheag.livejournal_telegrambot.job;

import com.github.luchbheag.livejournal_telegrambot.service.FindNewArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Job for finding new articles.
 */
@Slf4j
@Component
public class FindNewArticleJob {
    private final FindNewArticleService findNewArticleService;

    @Autowired
    public FindNewArticleJob(FindNewArticleService findNewArticleService) {
        this.findNewArticleService = findNewArticleService;
    }

    @Scheduled(fixedRateString = "${bot.recountNewArticleFixedRate}")
    public void findNewArticles() {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("Find new article job started.");

        findNewArticleService.findNewArticles();

        LocalDateTime endTime = LocalDateTime.now();
        log.info("Find new article job finished. Took seconds: {}",
                endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC));

    }
}
