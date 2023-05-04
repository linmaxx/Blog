package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.TagDto;
import com.lzx.domain.dto.TagListDto;
import com.lzx.domain.entity.Tag;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.mapper.TagMapper;
import com.lzx.domain.service.TagService;
import com.lzx.domain.vo.PageVo;
import com.lzx.domain.vo.TagVo;
import com.lzx.exception.SystemException;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-09 10:44:24
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        String name = tagListDto.getName();
        String remark = tagListDto.getRemark();
        LambdaQueryWrapper<Tag> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(name),Tag::getName,name);
        lambdaQueryWrapper.eq(StringUtils.hasText(remark),Tag::getRemark,remark);
        Page<Tag> page=new Page<>(pageNum,pageSize);
        this.page(page, lambdaQueryWrapper);
        List<Tag> records = page.getRecords();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(records, TagVo.class);
        return ResponseResult.okResult(new PageVo(tagVos, page.getTotal()));
    }

    @Override
    public ResponseResult addTag(TagListDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(List<Long> id) { //批量删除
        id.stream()
                .forEach(i->{
                    LambdaUpdateWrapper<Tag> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.set(Tag::getDelFlag, SystemConstants.DEL_FLAG);
                    lambdaUpdateWrapper.eq(Tag::getId,i);
                    update(lambdaUpdateWrapper);
                });
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> list = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}

