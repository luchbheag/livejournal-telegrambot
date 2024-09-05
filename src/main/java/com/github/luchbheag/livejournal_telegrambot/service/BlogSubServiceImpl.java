package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParserImpl;
import com.github.luchbheag.livejournal_telegrambot.repository.BlogSubRepository;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import jakarta.ws.rs.NotFoundException;
import org.jsoup.HttpStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogSubServiceImpl implements BlogSubService {

    private final BlogSubRepository blogSubRepository;
    private final TelegramUserService telegramUserService;
    private final LivejournalParser livejournalParser;

    @Autowired
    public BlogSubServiceImpl(BlogSubRepository blogSubRepository,
                              TelegramUserService telegramUserService,
                              LivejournalParser livejournalParser) {
        this.blogSubRepository = blogSubRepository;
        this.telegramUserService = telegramUserService;
        this.livejournalParser = livejournalParser;
    }

    @Override
    public BlogSub save(String chatId, String blogName) throws HttpStatusException, NotFoundException {
        // TODO exception message and exception handling
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        BlogSub blogSub;
        Optional<BlogSub> blogSubFromDB = blogSubRepository.findById(blogName);
        if (blogSubFromDB.isPresent()) {
            blogSub = blogSubFromDB.get();
            Optional<TelegramUser> first = blogSub.getUsers().stream()
                    .filter(it -> it.getChatId().equalsIgnoreCase(chatId))
                    .findFirst();
            if (first.isEmpty()) {
                blogSub.addUser(telegramUser);
            }
        } else {
            blogSub = new BlogSub();
            // what if there is no such a blog
            blogSub.addUser(telegramUser);
            blogSub.setId(blogName);
            ArticlePreview articlePreview = livejournalParser.getFirstArticlePreview(blogName);
            // here I should save this thing in DB as well, I guess. OR DOES IT WORK BY ITSELF?
            blogSub.setArticlePreview(articlePreview);
        }
        return blogSubRepository.save(blogSub);
    }
}
