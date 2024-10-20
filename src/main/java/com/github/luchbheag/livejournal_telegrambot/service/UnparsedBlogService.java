package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;

import java.util.List;
import java.util.Optional;

/**
 * Service for manipulation with {@link UnparsedBlog}
 */
public interface UnparsedBlogService {
    UnparsedBlog save(String chatId, String blogName);


   UnparsedBlog save(UnparsedBlog unparsedBlog);

    List<UnparsedBlog> findAll();

    Optional<UnparsedBlog> findById(String blogId);

    void delete(String blogId);
}
