package com.github.luchbheag.livejournal_telegrambot.repository;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.BlogSub;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

/**
 * Integration-level testing for {@link TelegramUserRepository}.
 */
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TelegramUserRepositoryIT {
    @Autowired
    private TelegramUserRepository telegramUserRepository;

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/telegram_users.sql"})
    @Test
    public void shouldProperlyFindAllActiveUsers() {
        // when
        List<TelegramUser> users = telegramUserRepository.findAllByActiveTrue();

        // then
        Assertions.assertEquals(5, users.size());
    }

    @Sql(scripts = {"/sql/clearDbs.sql"})
    @Test
    public void shouldProperlySaveTelegramUser() {
        // given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId("1234567890");
        telegramUser.setActive(true);
        telegramUserRepository.save(telegramUser);

        // when
        Optional<TelegramUser> saved = telegramUserRepository.findById(telegramUser.getChatId());

        // then
        Assertions.assertTrue(saved.isPresent());
        Assertions.assertEquals(telegramUser, saved.get());
    }

    @Sql(scripts = {"/sql/clearDbs.sql", "sql/fiveBlogSub.sql"})
    @Test
    public void shouldProperlyGetAllBlogSubsForUser() {
        // when
        Optional<TelegramUser> userFromDB = telegramUserRepository.findById("1");

        // then
        Assertions.assertTrue(userFromDB.isPresent());
        List<BlogSub> blogSubs = userFromDB.get().getBlogSubs();
        for (int i = 0; i < blogSubs.size(); i++) {
            Assertions.assertEquals(String.format("b%s", +1), blogSubs.get(i).getId());
            Assertions.assertEquals(i + 1, blogSubs.get(i).getArticleId());
        }
    }
}
