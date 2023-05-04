package com.lzx.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.CategoryDto;
import com.lzx.domain.entity.Category;

import javax.servlet.http.HttpServletResponse;

public interface CategoryService extends IService<Category> {
    ResponseResult categoryList();

    ResponseResult listAllCategory();

    void export(HttpServletResponse response);

    ResponseResult list(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(String name, String description, String status);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(CategoryDto categoryDto);

    ResponseResult deleteCategory(Long id);
}
