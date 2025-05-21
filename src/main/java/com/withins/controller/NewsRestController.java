package com.withins.controller;

import com.withins.response.NewsCondition;
import com.withins.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsRestController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<?> getNews(@PageableDefault Pageable pageable,
                                     @ModelAttribute NewsCondition condition) {
        var newsPageWith = newsService.search(pageable, condition);
        return ResponseEntity.ok(newsPageWith);
    }
}
