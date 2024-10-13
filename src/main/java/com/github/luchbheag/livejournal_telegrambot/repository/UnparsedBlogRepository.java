package com.github.luchbheag.livejournal_telegrambot.repository;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.UnparsedBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link Repository} for handling {@link UnparsedBlog} entity.
 */
@Repository
public interface UnparsedBlogRepository extends JpaRepository<UnparsedBlog, String> {
}
