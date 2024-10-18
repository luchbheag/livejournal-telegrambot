package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;

import java.util.List;

/**
 * Service for manipulation with {@link UnparsedBlog}
 */
public interface UnparsedBlogService {
    UnparsedBlog save(String chatId, String blogName);

    List<UnparsedBlog> findAll();
}
