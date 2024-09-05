package com.github.luchbheag.livejournal_telegrambot.repository;


import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link Repository} for {@link ArticlePreview} entity.
 */
@Repository
public interface ArticlePreviewRepository extends JpaRepository<ArticlePreview, Integer> {
}
