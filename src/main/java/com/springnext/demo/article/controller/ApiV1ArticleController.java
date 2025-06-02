package com.springnext.demo.article.controller;

import com.springnext.demo.article.controller.request.ArticleRequest;
import com.springnext.demo.article.entity.Article;
import com.springnext.demo.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
@Tag(name = "Blog", description = "Blog API Document")
public class ApiV1ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Article 작성", description = "Article을 작성합니다.")
    public ResponseEntity<Article> createArticle(@RequestBody ArticleRequest request) {
        Article savedArticle = articleService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Article 전체 조회", description = "전체 Article을 조회합니다.")
    public ResponseEntity<List<Article>> getArticles() {

        List<Article> articles = articleService.findAll();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        Article article = articleService.findById(id);

        return ResponseEntity.ok()
                .body(article);
    }
}
