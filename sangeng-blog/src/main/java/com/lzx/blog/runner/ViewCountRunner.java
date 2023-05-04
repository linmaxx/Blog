package com.lzx.blog.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.domain.entity.Article;
import com.lzx.domain.service.ArticleService;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Article::getId,Article::getViewCount);//查询 id 和count
        List<Article> list = articleService.list(lambdaQueryWrapper);
        //从数据库中查询 article的浏览量存入 redis
        Map<String, Integer> map = list.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        redisCache.setCacheMap("article:viewCount", map);

    }
}
