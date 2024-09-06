package com.github.luchbheag.livejournal_telegrambot.repository;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link Repository} for {@link BlogSub} entity.
 */
@Repository
public interface BlogSubRepository extends JpaRepository<BlogSub, String> {
}
