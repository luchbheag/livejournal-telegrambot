package com.github.luchbheag.livejournal_telegrambot.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "unparsed_blog_confirm")
@EqualsAndHashCode
@Setter
@Getter
public class ConfirmationInfo {
    @Id
    @Column(name = "user_id")
    String chatId;

    @Column(name = "blog_name")
    String blogName;
}
