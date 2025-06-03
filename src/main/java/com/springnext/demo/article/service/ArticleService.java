package com.springnext.demo.article.service;

import com.springnext.demo.article.controller.request.ArticleRequest;
import com.springnext.demo.article.entity.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Article save(ArticleRequest request);
    List<Article> findAll();
    Optional<Article> findById(Long id);
}
