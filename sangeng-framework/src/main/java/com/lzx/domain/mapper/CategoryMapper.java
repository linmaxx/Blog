package com.lzx.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.domain.entity.Category;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> getRightCategory();
}
