package com.github.luchbheag.livejournal_telegrambot.parser.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreview {
    private int id;

    private String mainHeader;

    private String subHeader;

    private String text;

    private String link;
}
