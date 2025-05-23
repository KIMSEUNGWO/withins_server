package com.withins.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TempNewsAddController {

    private final NewsService newsService;
    private final BCryptPasswordEncoder encoder;

    @GetMapping("/encode")
    public ResponseEntity<String> member(@RequestParam String p) {
        return ResponseEntity.ok(encoder.encode(p));
    }

    @GetMapping("/news")
    public ResponseEntity<String> addingNews() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path dataPath = Paths.get("data");
        Files.list(dataPath)
            .filter(Files::isRegularFile)
            .forEach(path -> {
                try {
                    Map<String, Object> map = mapper.readValue(path.toFile(), Map.class);
                    newsService.saveAllCrawlData(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        return ResponseEntity.ok("OK");
    }

    private Map<String, Object> loadJsonWithResourceUtils() throws IOException {
        File file = new File("results.json"); // 프로젝트 루트의 파일 참조
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Map.class);
    }

}
