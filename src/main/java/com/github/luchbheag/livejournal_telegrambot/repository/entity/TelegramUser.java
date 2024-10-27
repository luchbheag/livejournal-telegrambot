package com.github.luchbheag.livejournal_telegrambot.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;


/**
 * Telegram User entity.
 */
@Data
@Entity
@Table(name = "tg_user")
@EqualsAndHashCode(exclude = {"blogSubs", "unparsedBlogs"})
public class TelegramUser {
    @Id
    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<BlogSub> blogSubs;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<UnparsedBlog> unparsedBlogs;

    public void addBlogSub(BlogSub blogSub) {
        if (isNull(blogSubs)) {
            blogSubs = new ArrayList<>();
        }
        blogSubs.add(blogSub);
    }

//    public void addUnparsedBlog(UnparsedBlog unparsedBlog) {
//        if (isNull(unparsedBlogs)) {
//            unparsedBlogs = new ArrayList<>();
//        }
//        unparsedBlogs.add(unparsedBlog);
//    }
}
