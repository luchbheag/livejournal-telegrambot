package com.github.luchbheag.livejournal_telegrambot.parser;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.HttpStatusException;

import java.util.List;

/**
 * Parser for getting articles from liverjournal.com
 */
public interface LivejournalParser {

    // TODO: refactor journalName to blogName
    public ArticlePreview getFirstArticlePreview(String journalName) throws HttpStatusException;

    public List<ArticlePreview> getAllArticlePreviewsSinceId(String journalName, int id);

    public void setLimit(int limit);

    public int getLimit();
}
