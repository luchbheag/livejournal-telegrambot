package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParserImpl;
import com.github.luchbheag.livejournal_telegrambot.repository.BlogSubRepository;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.ArticlePreview;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

// TODO cause logic is a bit different
@DisplayName("Unit-level testing for BlogSubService")
public class BlogSubServiceTest {
//
//    private BlogSubService blogSubService;
//    private BlogSubRepository blogSubRepository;
//    private TelegramUser testUser;
//
//    private final static String CHAT_ID = "1";
//
//    @BeforeEach
//    public void init() {
//        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
//        blogSubRepository = Mockito.mock(BlogSubRepository.class);
//        blogSubService = new BlogSubServiceImpl(blogSubRepository, telegramUserService);
//        LivejournalParser livejournalParser = new LivejournalParserImpl();
//
//        testUser = new TelegramUser();
//        testUser.setChatId(CHAT_ID);
//        testUser.setActive(true);
//
//        Mockito.when(telegramUserService.findByChatId(CHAT_ID))
//                .thenReturn(Optional.of(testUser));
//        Mockito.when(livejournalParser.getFirstArticlePreview("1"))
//                .thenReturn(new ArticlePreview(1, "Main header",
//                        "Sub header", "tet", "http://link.com"));
//    }
//
//    @Test
//    public void shouldProperlySaveGroup() {
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
//        blogSubRepository.save(CHAT_ID, "1");
//
//        // then
//    }
}
