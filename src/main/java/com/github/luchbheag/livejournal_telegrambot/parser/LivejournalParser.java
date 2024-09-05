package com.github.luchbheag.livejournal_telegrambot.parser;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import org.jsoup.HttpStatusException;

import java.util.List;

/**
 * Parser for getting articles from liverjournal.com
 */
public interface LivejournalParser {

    public ArticlePreview getFirstArticlePreview(String journalName) throws HttpStatusException;

    public List<ArticlePreview> getAllArticlePreviewsSinceId(String journalName, int id);
}
