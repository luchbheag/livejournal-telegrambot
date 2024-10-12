package com.github.luchbheag.livejournal_telegrambot.parser;

import com.github.luchbheag.livejournal_telegrambot.parser.dto.ArticlePreview;
import com.github.luchbheag.livejournal_telegrambot.parser.excpection.CannotParsePageException;
import org.jsoup.HttpStatusException;

import java.util.List;

/**
 * Parser for getting articles from liverjournal.com
 */
public interface LivejournalParser {

    // TODO: refactor journalName to blogName
    public int getLastArticleId(String journalName) throws HttpStatusException, CannotParsePageException;

    public List<ArticlePreview> getAllArticlePreviewsSinceId(String journalName, int id) throws CannotParsePageException;

    public void setLimit(int limit);

    public int getLimit();
}
