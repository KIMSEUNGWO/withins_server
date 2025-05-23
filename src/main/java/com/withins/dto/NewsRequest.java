package com.withins.dto;

import com.withins.enums.NewsType;

import java.time.LocalDate;

public record NewsRequest(Long newsId,
                          String title,
                          NewsType type,
                          String link,
                          OrgRequest organization,
                          LocalDate createdAt) {
}
