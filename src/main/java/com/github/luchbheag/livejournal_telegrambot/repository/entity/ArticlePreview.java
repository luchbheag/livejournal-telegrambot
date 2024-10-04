package com.github.luchbheag.livejournal_telegrambot.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

// TODO rename class OR table in db for match
@Data
@Entity
@Table(name = "article_previews")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreview {
    /* TODO: Store only articleId and BlogName (header, subheader and first piece of text can be took from that.
       TODO: And you can also get list of ArticlePreviews, but save only last one in DB
     */
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "main_header")
    private String mainHeader;

    @Column(name = "sub_header")
    private String subHeader;

    @Column(name = "text")
    private String text;

    @Column(name = "link")
    private String link;
}
