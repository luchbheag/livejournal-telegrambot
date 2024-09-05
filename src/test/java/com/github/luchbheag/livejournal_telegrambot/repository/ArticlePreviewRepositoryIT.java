package com.github.luchbheag.livejournal_telegrambot.repository;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

/**
 * Integration-level testing for {@link ArticlePreviewRepository}.
 */
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticlePreviewRepositoryIT {

    @Autowired
    private ArticlePreviewRepository articlePreviewRepository;

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/article_previews.sql"})
    @Test
    public void shouldProperlyFindArticlesPreview() {
        // when
        List<ArticlePreview> articlePreviews = articlePreviewRepository.findAll();

        // then
        Assertions.assertEquals(3, articlePreviews.size());
    }
}
