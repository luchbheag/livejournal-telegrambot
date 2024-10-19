package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.parser.LivejournalParser;
import com.github.luchbheag.livejournal_telegrambot.parser.excpection.CannotParsePageException;
import com.github.luchbheag.livejournal_telegrambot.repository.BlogSubRepository;
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
    public void init() throws HttpStatusException, NotFoundException, CannotParsePageException {
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        blogSubRepository = Mockito.mock(BlogSubRepository.class);
        LivejournalParser livejournalParser = Mockito.mock(LivejournalParser.class);
        blogSubService = new BlogSubServiceImpl(blogSubRepository, telegramUserService,
                livejournalParser);

        testUser = new TelegramUser();
        testUser.setChatId(CHAT_ID);
        testUser.setActive(true);

        Mockito.when(telegramUserService.findByChatId(CHAT_ID))
                .thenReturn(Optional.of(testUser));
        Mockito.when(livejournalParser.getLastArticleId("1"))
                .thenReturn(1);
    }

    @Test
    public void shouldProperlySaveBlogAsEntity() {
        // given
        BlogSub expectedBlogSub = new BlogSub();
        expectedBlogSub.setId("1");
        expectedBlogSub.setLastArticleId(1);
        expectedBlogSub.addUser(testUser);

        // when
        blogSubService.save(expectedBlogSub);

        // then
        Mockito.verify(blogSubRepository).save(expectedBlogSub);
    }

    @Test
    public void shouldProperlyAddUserToExistingBlog() throws HttpStatusException, NotFoundException, CannotParsePageException {
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
