package com.withins.dto;

import com.withins.enums.NewsType;

import java.time.LocalDateTime;

public record NewsRequest(Long newsId,
                          String title,
                          NewsType type,
                          String link,
                          OrgRequest organization,
                          LocalDateTime createdAt) {
}
