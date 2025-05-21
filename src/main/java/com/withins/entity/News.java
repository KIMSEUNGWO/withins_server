package com.withins.entity;

import com.withins.enums.NewsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
public class News {

    @Column(name = "newsId")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long baseId;
    private String title;
    @Enumerated(EnumType.STRING)
    private NewsType type;
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId")
    private Organization organization;
    private LocalDateTime createdAt;

}