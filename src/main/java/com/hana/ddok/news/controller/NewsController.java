package com.hana.ddok.news.controller;

import com.hana.ddok.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "News", description = "뉴스 API")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/news")
    @Operation(summary = "뉴스 검색")
    public ResponseEntity<String> searchNews(@RequestParam String query) {
        String response = newsService.searchNews(query);
        return ResponseEntity.ok(response);
    }
}
