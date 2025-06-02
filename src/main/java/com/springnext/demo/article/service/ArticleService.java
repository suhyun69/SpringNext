package com.springnext.demo.article.service;

import com.springnext.demo.article.controller.request.ArticleRequest;
import com.springnext.demo.article.entity.Article;

import java.util.List;

public interface ArticleService {
    Article save(ArticleRequest request);
    List<Article> findAll();
    Article findById(Long id);
}
