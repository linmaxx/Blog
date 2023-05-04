package com.lzx.domain.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.CategoryDto;
import com.lzx.domain.entity.Article;
import com.lzx.domain.entity.Category;
import com.lzx.domain.entity.ExcelCategoryVo;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.mapper.ArticleMapper;
import com.lzx.domain.mapper.CategoryMapper;
import com.lzx.domain.service.CategoryService;
import com.lzx.domain.vo.CategoryVo;
import com.lzx.domain.vo.PageVo;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.WebUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public ResponseResult categoryList() {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //查询正常状态的文章
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleMapper.selectList(lambdaQueryWrapper);
        Set<Long> cateforiId = articles.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());  //转换成set 直接去重
        //查找分类表
        List<Category> categories = listByIds(cateforiId);
        List<Category> categoryList = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        List<Category> list = list();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> list = list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            //把数据写入到Excel中
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            response.reset();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.hasText(name), Category::getName,name);
        queryWrapper.eq(Strings.hasText(status), Category::getStatus,status);
        Page<Category> page=new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Category> records = page.getRecords();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(records, CategoryVo.class);
        return ResponseResult.okResult(new PageVo(categoryVos,page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(String name, String description, String status) {
        Category category=new Category();
        category.setName(name);
        category.setDescription(description);
        category.setStatus(status);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        LambdaUpdateWrapper<Category> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.set(Category::getDelFlag,SystemConstants.DEL_FLAG);
        updateWrapper.eq(Category::getId,id);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

}
