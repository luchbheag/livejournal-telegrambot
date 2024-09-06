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
// TODO split ArticlePreview and GroupSub
@Data
@Entity
@Table(name = "article_previews")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreview {
    // TODO: I don't need to store all of that as the link contains ID. Let's think about it.
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
