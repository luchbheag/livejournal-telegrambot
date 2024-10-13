package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.repository.UnparsedBlogRepository;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnparsedBlogServiceImpl implements UnparsedBlogService {
    private final TelegramUserService telegramUserService;
    private final UnparsedBlogRepository unparsedBlogRepository;

    @Autowired
    public UnparsedBlogServiceImpl(TelegramUserService telegramUserService,
                                   UnparsedBlogRepository unparsedBlogRepository) {
        this.telegramUserService = telegramUserService;
        this.unparsedBlogRepository = unparsedBlogRepository;
    }

    @Override
    public UnparsedBlog save(String chatId, String blogName) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        UnparsedBlog unparsedBlog;
        Optional<UnparsedBlog> unparsedBlogFromDB = unparsedBlogRepository.findById(blogName);
        if (unparsedBlogFromDB.isPresent()) {
            unparsedBlog = unparsedBlogFromDB.get();
            Optional<TelegramUser> first = unparsedBlog.getUsers().stream()
                    .filter(it -> it.getChatId().equalsIgnoreCase(chatId))
                    .findFirst();
            if (first.isEmpty()) {
                unparsedBlog.addUser(telegramUser);
            }
        } else {
            unparsedBlog = new UnparsedBlog();
            unparsedBlog.setId(blogName);
            unparsedBlog.addUser(telegramUser);
        }
        return unparsedBlogRepository.save(unparsedBlog);
        }
    }
