package com.hana.ddok.news.controller;

import com.hana.ddok.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/news")
    public ResponseEntity<String> searchNews(@RequestParam String query) {
        String response = newsService.searchNews(query);
        return ResponseEntity.ok(response);
    }
}
