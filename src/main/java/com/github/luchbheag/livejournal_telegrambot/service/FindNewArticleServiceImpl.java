package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.parser.dto.ArticlePreview;
import com.github.luchbheag.livejournal_telegrambot.parser.excpection.CannotParsePageException;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

// TODO: get rid of articlePreview in DB (left it only for parser). BUT! It's better not to store it in BlogSub OR create a second BlogSub for old ones
@Service
public class FindNewArticleServiceImpl implements FindNewArticleService {

    private final BlogSubService blogSubService;
    private final LivejournalParser livejournalParser;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewArticleServiceImpl(BlogSubService blogSubService,
                                     LivejournalParser livejournalParser,
                                     SendBotMessageService sendBotMessageService) {
        this.blogSubService = blogSubService;
        this.livejournalParser = livejournalParser;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewArticles() {
        blogSubService.findAll().forEach(blogSub -> {
            try {
                List<ArticlePreview> newArticles = livejournalParser.getAllArticlePreviewsSinceId(blogSub.getId(), blogSub.getLastArticleId());
                setNewArticlePreview(blogSub, newArticles);
                notifySubscribersAboutNewArticles(blogSub, newArticles);
            } catch (CannotParsePageException e) {
                e.printStackTrace();
            }
        });
    }

    private void notifySubscribersAboutNewArticles(BlogSub blogSub, List<ArticlePreview> newArticles) {
        Collections.reverse(newArticles);
        List<String> messagesWithNewArticles = newArticles.stream()
                .map(article -> String.format("There is a new article in the blog %s.\n\n"
                + "<b>%s</b>\n"
                + "%s\n\n"
                + "%s\n"
                + "Link: %s", article.getMainHeader(), article.getSubHeader(),
                        article.getText(), article.getLink()))
                .toList();

        blogSub.getUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessages(it.getChatId(), messagesWithNewArticles));
    }

    private void setNewArticlePreview(BlogSub blogSub, List<ArticlePreview> newArticles) {
        if (!newArticles.isEmpty()) {
            blogSub.setLastArticleId(newArticles.get(0).getId());
        }
    }

}
