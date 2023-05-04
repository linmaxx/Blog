package com.lzx.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.TagDto;
import com.lzx.domain.dto.TagListDto;
import com.lzx.domain.entity.Tag;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-03-09 10:44:23
 */
public interface TagService extends IService<Tag> {

    ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(TagListDto tagDto);

    ResponseResult deleteTag(List<Long> id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(TagDto tagDto);

    ResponseResult listAllTag();
}

