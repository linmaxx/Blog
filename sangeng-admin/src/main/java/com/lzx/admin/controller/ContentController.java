package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddArticleDto;
import com.lzx.domain.service.ArticleService;
import com.lzx.domain.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RunAs;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ArticleService articleService;
    @PostMapping("/article")  // 写博文的接口
    public ResponseResult article(@RequestBody AddArticleDto addArticleDto){
        return articleService.addArticle(addArticleDto);
    }

}
