package com.lzx.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.LinkDto;
import com.lzx.domain.entity.Link;

import java.util.List;

public interface LinkService extends IService<Link> {
    ResponseResult getAllLink();

    ResponseResult list(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(LinkDto linkDto);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(LinkDto linkDto);

    ResponseResult deleteLink(List<Long> ids);

    ResponseResult changeLinkStatus(Long id, String status);
}
