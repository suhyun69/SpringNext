package com.springnext.demo.article.controller;

import com.springnext.demo.article.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ApiV1ArticleController {

    @GetMapping("")
    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article(1L));
        articles.add(new Article(2L));
        articles.add(new Article(3L));

        return articles;
    }
}
