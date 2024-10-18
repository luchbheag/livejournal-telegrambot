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
@Table(name = "unparsed_blog")
@EqualsAndHashCode
@Setter
@Getter
public class UnparsedBlog {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "unparsed_x_user",
            joinColumns = @JoinColumn(name = "unparsed_blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<TelegramUser> users;

    public void addUser(TelegramUser telegramUser) {
        if (isNull(users)) {
            users = new ArrayList<>();
        }
        users.add(telegramUser);
    }

    public int compareTo(UnparsedBlog unparsedBlog) {
        if (isNull(unparsedBlog.getUsers()) && isNull(users)) {
            return this.id.compareTo(unparsedBlog.id);
        }
        if (isNull(this.getUsers())) {
            return -1;
        } else if (isNull(unparsedBlog.getUsers())) {
            return 1;
        } else {
            int result = users.size() - unparsedBlog.getUsers().size();
            return result == 0 ? this.getId().compareTo(unparsedBlog.getId()) : result;
        }
    }
}
