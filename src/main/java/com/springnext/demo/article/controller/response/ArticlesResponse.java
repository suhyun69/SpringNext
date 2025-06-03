package com.springnext.demo.article.controller.response;

import com.springnext.demo.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticlesResponse {
    private List<Article> articles;
}
