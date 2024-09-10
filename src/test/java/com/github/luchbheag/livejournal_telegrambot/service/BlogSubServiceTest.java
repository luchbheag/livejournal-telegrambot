package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParserImpl;
import com.github.luchbheag.livejournal_telegrambot.repository.ArticlePreviewRepository;
import com.github.luchbheag.livejournal_telegrambot.repository.BlogSubRepository;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import jakarta.ws.rs.NotFoundException;
import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

@DisplayName("Unit-level testing for BlogSubService")
public class BlogSubServiceTest {

    private BlogSubService blogSubService;
    private BlogSubRepository blogSubRepository;
    private TelegramUser testUser;

    private final static String CHAT_ID = "1";

    @BeforeEach
    public void init() throws HttpStatusException, NotFoundException {
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        blogSubRepository = Mockito.mock(BlogSubRepository.class);
        LivejournalParser livejournalParser = Mockito.mock(LivejournalParser.class);
        ArticlePreviewRepository articlePreviewRepository = Mockito.mock(ArticlePreviewRepository.class);
        blogSubService = new BlogSubServiceImpl(blogSubRepository, telegramUserService,
                livejournalParser, articlePreviewRepository);

        testUser = new TelegramUser();
        testUser.setChatId(CHAT_ID);
        testUser.setActive(true);

        Mockito.when(telegramUserService.findByChatId(CHAT_ID))
                .thenReturn(Optional.of(testUser));
        Mockito.when(livejournalParser.getFirstArticlePreview("1"))
                .thenReturn(new ArticlePreview(1, "Main header",
                        "Sub header", "tet", "http://link.com"));
    }
    // TODO
//    @Test
//    public void shouldProperlySaveBlog() {
//        // given
//        ArticlePreview articlePreview = new ArticlePreview();
//        articlePreview.setId(1);
//        articlePreview.setMainHeader("Main header");
//        articlePreview.setSubHeader("Sub header");
//        articlePreview.setText("text");
//        articlePreview.setLink("https://link.com");
//
//        BlogSub expectedBlogSub = new BlogSub();
//        expectedBlogSub.setId("1");
//        expectedBlogSub.setArticlePreview(articlePreview);
//        expectedBlogSub.addUser(testUser);
//
//        // when
//        blogSubRepository.save("1");
//
//        // then
//        Mockito.verify(blogSubRepository).save(expectedBlogSub);
//    }

    @Test
    public void shouldProperlyAddUserToExistingBlog() throws HttpStatusException, NotFoundException {
        // given
        TelegramUser oldTelegramUser = new TelegramUser();
        oldTelegramUser.setChatId("2");
        oldTelegramUser.setActive(true);

        String blogSubId = "1";
        BlogSub blogSubFromDB = new BlogSub();
        blogSubFromDB.setId(blogSubId);
        blogSubFromDB.addUser(oldTelegramUser);

        Mockito.when(blogSubRepository.findById(blogSubId)).thenReturn(Optional.of(blogSubFromDB));

        BlogSub expectedBlogSub = new BlogSub();
        expectedBlogSub.setId(blogSubId);
        expectedBlogSub.addUser(oldTelegramUser);
        expectedBlogSub.addUser(testUser);

        // when
        blogSubService.save(CHAT_ID, blogSubId);

        // then
        Mockito.verify(blogSubRepository).findById(blogSubId);
        Mockito.verify(blogSubRepository).save(expectedBlogSub);
    }
}
