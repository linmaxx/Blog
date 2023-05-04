package com.lzx.admin.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AdminUpdateArticleDto;
import com.lzx.domain.entity.Article;
import com.lzx.domain.service.ArticleService;
import com.lzx.domain.vo.ArticleVo;
import com.lzx.domain.vo.PageVo;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String title,String summary){
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.like(StringUtils.hasText(title),Article::getTitle, title).
                or().like(StringUtils.hasText(summary),Article::getSummary, summary);
        Page<Article> page=new Page<>(pageNum,pageSize);
        articleService.page(page, lambdaUpdateWrapper);
        List<Article> records = page.getRecords();
        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(records, ArticleVo.class);
        return ResponseResult.okResult(new PageVo(articleVos,page.getTotal()));
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") long id){
        return articleService.adminArticleDetail(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody AdminUpdateArticleDto articleDto){
        return articleService.adminUpdateArticle(articleDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult deleteArticle(@PathVariable("ids") List<Long> ids){
        return articleService.adminDeleteArticle(ids);
    }
}
