package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddArticleDto;
import com.lzx.domain.dto.AdminUpdateArticleDto;
import com.lzx.domain.entity.Article;
import com.lzx.domain.entity.ArticleTag;
import com.lzx.domain.mapper.ArticleMapper;
import com.lzx.domain.mapper.ArticleTagMapper;
import com.lzx.domain.service.ArticleService;
import com.lzx.domain.service.CategoryService;
import com.lzx.domain.vo.*;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagMapper articleTagMapper; // 实现添加博文需要用到

    @Override
    //查询热门文章 封装成 ResponseResult 返回
    public ResponseResult hotArticleList() {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章，不可以是草稿，即状态为0
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览数目降序排列
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多显示10条
        Page<Article> page=new Page<>(1,10);
        page(page,queryWrapper); // Iservice 实现的方法
        List<Article> articles = page.getRecords();
        //怎么封装成 Article 对应的 HotArticleVo 使用bean拷贝
        List<HotArticleVo> hotArticleVoList = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
//        for(Article article : articles){
//            HotArticleVo hotArticleVo=new HotArticleVo();
//            //这里的拷贝是依靠 article 和 hotArticleVo 中的属性名和属性类型一致进行拷贝的
//            BeanUtils.copyProperties(article,hotArticleVo);
//            hotArticleVos.add(hotArticleVo);
//        }
        return ResponseResult.okResult(hotArticleVoList);
    }

    @Override
    public ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        if(categoryId !=null && categoryId > 0){
//            lambdaQueryWrapper.eq(Article::getCategoryId,categoryId);
//        }
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0, Article::getCategoryId,categoryId);
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop); //按照置顶排序
        Page<Article> page=new Page<>(pageNum,pageSize);
        page(page, lambdaQueryWrapper);
        List<Article> articles = page.getRecords();
        List<Article> list = articles.stream() //对流中的元素进行计算或者替换
                .map(article -> {
                    article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                    return article;
                })
                .collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(list, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
        ArticleDetailVo articleVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //从 redis 中读取 viewCount
        Map<String, Integer> viewMap = redisCache.getCacheMap("article:viewCount");
        Integer viewCount = viewMap.get("" + id);
        articleVo.setViewCount(viewCount.longValue());
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新浏览量时去更新redis中的数据
        redisCache.incrementCacheMapValue("article:viewCount", ""+id,1);
        return ResponseResult.okResult();
    }

    //写博客操作
    @Override
    @Transactional  //有两次 操纵数据库时 就要加上事务注解
    public ResponseResult addArticle(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        List<Long> tags = addArticleDto.getTags();
        //将文章 保存到 article 表中
        save(article);
        System.out.println(article.getId());
        //将文章对应的标签保存到 article_tag 种
        tags.stream()
                .forEach(tag->articleTagMapper.insert(new ArticleTag(article.getId(), tag)));
        return ResponseResult.okResult();
    }
    //后台文章管理中 在修改博客过程中的回显原博客数据
    @Override
    public ResponseResult adminArticleDetail(long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper=new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,id);
        articleTagLambdaQueryWrapper.select(ArticleTag::getTagId);
        List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
        List<Long> tags = articleTags.stream()
                .map(tag -> tag.getTagId())
                .collect(Collectors.toList());
        AdminArticleVo adminArticleVo = BeanCopyUtils.copyBean(article, AdminArticleVo.class);
        adminArticleVo.setTags(tags);
        return ResponseResult.okResult(adminArticleVo);
    }

    //后台文章管理中 在修改博客过程中的更新博客数据
    @Override
    public ResponseResult adminUpdateArticle(AdminUpdateArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        List<Long> tags = articleDto.getTags();
        //将文章 保存到 article 表中
        updateById(article);
        //将文章对应的标签保存到 article_tag 种
        //删除该文章原有的tag_id
        LambdaUpdateWrapper<ArticleTag> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagMapper.delete(lambdaUpdateWrapper);
        tags.stream()
                .forEach(tag->articleTagMapper.insert(new ArticleTag(article.getId(), tag)));
        return ResponseResult.okResult();
    }

    //后台文章管理中 删除博客数据
    @Override
    public ResponseResult adminDeleteArticle(List<Long> ids) {
        ids.stream()
                .forEach(id->{
                    LambdaUpdateWrapper<Article> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.set(Article::getDelFlag, SystemConstants.DEL_FLAG);
                    lambdaUpdateWrapper.eq(Article::getId,id);
                    update(lambdaUpdateWrapper);
                });
        return ResponseResult.okResult();
    }
}
