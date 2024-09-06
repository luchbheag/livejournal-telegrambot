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
public class BlogSubRepositoryIT {

    @Autowired
    private BlogSubRepository blogSubRepository;

    @Sql(scripts = {"/sql/clearDbs.sql", "sql/fiveBlogSub.sql"})
    @Test
    public void shouldProperlyGetAllBlogSubsForUser() {
        // when
        Optional<BlogSub> blogSubFromDB = blogSubRepository.findById("b1");

        // then
        Assertions.assertTrue(blogSubFromDB.isPresent());
        Assertions.assertEquals(1, blogSubFromDB.get().getId());
        List<TelegramUser> users = blogSubFromDB.get().getUsers();
        for(int i=0; i<users.size(); i++) {
            Assertions.assertEquals(String.valueOf(i + 1), users.get(i).getChatId());
            Assertions.assertTrue(users.get(i).isActive());
        }
    }
}
