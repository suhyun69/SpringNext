package com.springnext.demo.global;

import com.springnext.demo.article.controller.request.ArticleRequest;
import com.springnext.demo.article.service.ArticleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProd {

    @Bean
    CommandLineRunner initData(ArticleService articleService) {
        return args -> {
            articleService.save(new ArticleRequest("제목1", "내용1"));
            articleService.save(new ArticleRequest("제목2", "내용2"));
            articleService.save(new ArticleRequest("제목3", "내용3"));
            articleService.save(new ArticleRequest("제목4", "내용4"));
            articleService.save(new ArticleRequest("제목5", "내용5"));
        };
    }
}
