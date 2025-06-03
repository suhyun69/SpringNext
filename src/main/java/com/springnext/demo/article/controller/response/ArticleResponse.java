package com.springnext.demo.article.controller.response;

import com.springnext.demo.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    private Article article;
}
