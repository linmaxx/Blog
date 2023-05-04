package com.lzx.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddArticleDto;
import com.lzx.domain.dto.AdminUpdateArticleDto;
import com.lzx.domain.entity.Article;

import java.util.List;

public interface ArticleService  extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto addArticleDto);

    ResponseResult adminArticleDetail(long id);

    ResponseResult adminUpdateArticle(AdminUpdateArticleDto articleDto);

    ResponseResult adminDeleteArticle(List<Long> id);
}
