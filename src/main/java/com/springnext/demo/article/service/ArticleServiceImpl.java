package com.springnext.demo.article.service;

import com.springnext.demo.article.controller.request.ArticleRequest;
import com.springnext.demo.article.entity.Article;
import com.springnext.demo.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public Article save(ArticleRequest request) {
        Article article = new Article(request.getSubject(), request.getContent());
        return articleRepository.save(article);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }
}
