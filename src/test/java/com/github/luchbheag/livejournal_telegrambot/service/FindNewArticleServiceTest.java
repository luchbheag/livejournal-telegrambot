package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.parser.dto.ArticlePreview;
import com.github.luchbheag.livejournal_telegrambot.parser.excpection.CannotParsePageException;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Unit-level testing for FindNewArticleService")
public class FindNewArticleServiceTest {

    private BlogSubService blogSubService;
    private LivejournalParser livejournalParser;
    private SendBotMessageService sendBotMessageService;
    private FindNewArticleService findNewArticleService;

    @BeforeEach
    public void init() {
        blogSubService = Mockito.mock(BlogSubService.class);
        livejournalParser = Mockito.mock(LivejournalParser.class);
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        findNewArticleService = new FindNewArticleServiceImpl(blogSubService, livejournalParser, sendBotMessageService);
    }

    @Test
    public void shouldCallNothingIfNoBlogSubs() {
        // given
        Mockito.when(blogSubService.findAll()).thenReturn(new ArrayList<>());

        // when
        findNewArticleService.findNewArticles();

        // then
        Mockito.verify(blogSubService).findAll();
        Mockito.verifyNoInteractions(livejournalParser);
        Mockito.verifyNoMoreInteractions(sendBotMessageService);
        Mockito.verifyNoInteractions(sendBotMessageService);
    }

    @Test
    public void shouldProperlySetNewArticleIdAndNotifyUser() throws CannotParsePageException {
        // given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(String.valueOf(123456L));
        telegramUser.setActive(true);
        String blogId = "test";
        BlogSub blogSub = new BlogSub();
        blogSub.setId(blogId);
        blogSub.setLastArticleId(1);
        blogSub.addUser(telegramUser);
        telegramUser.addBlogSub(blogSub);
        ArticlePreview articlePreview = new ArticlePreview();
        articlePreview.setId(2);
        articlePreview.setMainHeader("Main Header");
        articlePreview.setSubHeader("Sub Header");
        articlePreview.setText("text");
        articlePreview.setLink("http://www.link.com");

        Mockito.when(blogSubService.findAll()).thenReturn(List.of(blogSub));
        Mockito.when(livejournalParser.getAllArticlePreviewsSinceId(blogId, 1))
                .thenReturn(List.of(articlePreview));
        String expectedMessage = String.format("There is a new article in the blog %s.\n\n"
                        + "<b>%s</b>\n"
                        + "%s\n\n"
                        + "%s\n"
                        + "Link: %s", blogSub.getId(), articlePreview.getMainHeader(), articlePreview.getSubHeader(),
                articlePreview.getText(), articlePreview.getLink());

        // when
        findNewArticleService.findNewArticles();

        // then
        Mockito.verify(blogSubService).findAll();
        Mockito.verify(livejournalParser).getAllArticlePreviewsSinceId(blogId, 1);
        blogSub.setLastArticleId(2);
        Mockito.verify(blogSubService).save(blogSub);
        Mockito.verify(sendBotMessageService).sendMessages(telegramUser.getChatId(), List.of(expectedMessage));
    }
}
