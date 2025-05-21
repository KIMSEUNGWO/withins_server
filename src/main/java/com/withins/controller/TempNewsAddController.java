package com.withins.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TempNewsAddController {

    private final NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<String> addingNews() throws IOException {
        Map<String, Object> stringObjectMap = loadJsonWithResourceUtils();
        newsService.saveAllCrawlData(stringObjectMap);

        return ResponseEntity.ok("OK");
    }

    private Map<String, Object> loadJsonWithResourceUtils() throws IOException {
        File file = new File("results.json"); // 프로젝트 루트의 파일 참조
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Map.class);
    }

}
