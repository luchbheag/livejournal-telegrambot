package com.github.luchbheag.livejournal_telegrambot.parser;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Integration-level testing for LivejournalParser")
public class LivejournalParserTest {

    private final LivejournalParser parser = new LivejournalParserImpl();

    @Test
    public void shouldProperlyGetNew15Articles() {
        // when
        parser.setLimit(15);
        List<ArticlePreview> newArticles = parser.getAllArticlePreviewsSinceId("all-decoded", 0);

        // then
        Assertions.assertEquals(15, newArticles.size());
    }
}
