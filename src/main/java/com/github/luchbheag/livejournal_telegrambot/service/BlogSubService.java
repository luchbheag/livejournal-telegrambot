package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import jakarta.ws.rs.NotFoundException;
import org.jsoup.HttpStatusException;

import java.util.Optional;

/**
 * Service for manipulation with {@link BlogSub}
 */
public interface BlogSubService {
    BlogSub save(String chatId, String blogName) throws HttpStatusException, NotFoundException;

    BlogSub save(BlogSub blogSub);

    Optional<BlogSub> findById(String id);

    void delete(String id);
}
