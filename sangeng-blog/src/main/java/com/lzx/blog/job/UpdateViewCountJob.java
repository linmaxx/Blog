package com.lzx.blog.job;

import com.lzx.domain.entity.Article;
import com.lzx.domain.service.ArticleService;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    //没隔10分钟，将浏览量跟信道数据库
    @Scheduled(cron = "0/10 * * * * ?")
    public void updateViewCount(){
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");
        List<Article> articles = cacheMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        articleService.updateBatchById(articles);
    }

}
