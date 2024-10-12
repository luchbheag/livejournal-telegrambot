package com.github.luchbheag.livejournal_telegrambot.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Entity
@Table(name = "blog_sub")
@EqualsAndHashCode
@Setter
@Getter
public class BlogSub {
    // TODO: make Blog as an abstract class?
    @Id
    @Column(name = "id")
    private String id;

    private int lastArticleId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "blog_x_user",
            joinColumns = @JoinColumn(name = "blog_sub_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<TelegramUser> users;

    public void addUser(TelegramUser telegramUser) {
        if (isNull(users)) {
            users = new ArrayList<>();
        }
        users.add(telegramUser);
    }
}
