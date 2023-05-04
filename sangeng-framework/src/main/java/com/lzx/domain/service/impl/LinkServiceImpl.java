package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.LinkDto;
import com.lzx.domain.entity.Category;
import com.lzx.domain.entity.Link;
import com.lzx.domain.mapper.Linkmapper;
import com.lzx.domain.service.LinkService;
import com.lzx.domain.vo.CategoryVo;
import com.lzx.domain.vo.LinkVo;
import com.lzx.domain.vo.PageVo;
import com.lzx.utils.BeanCopyUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class LinkServiceImpl extends ServiceImpl<Linkmapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper=new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_PASS);
        List<Link> links = list(linkLambdaQueryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.hasText(name), Link::getName, name);
        queryWrapper.eq(Strings.hasText(status),Link::getStatus,status);
        Page<Link> page=new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Link> records = page.getRecords();
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(records, LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos,page.getTotal()));
    }

    @Override
    public ResponseResult addLink(LinkDto linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(LinkDto linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(List<Long> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(Long id, String status) {
        LambdaUpdateWrapper<Link> linkLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        linkLambdaUpdateWrapper.eq(Link::getId,id);
        linkLambdaUpdateWrapper.set(Link::getStatus,status);
        update(linkLambdaUpdateWrapper);
        return ResponseResult.okResult();
    }
}
