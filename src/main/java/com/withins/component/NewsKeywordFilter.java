package com.withins.component;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NewsKeywordFilter {

    private final Set<String> filters = Set.of("합격자");

    public boolean filter(String text) {
        if (text == null || text.trim().isEmpty()) {
            return true; // 빈 문자열은 통과
        }

        return filters.stream()
            .noneMatch(text::contains);
    }

}
