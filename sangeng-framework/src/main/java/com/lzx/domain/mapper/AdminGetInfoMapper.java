package com.lzx.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AdminGetInfoMapper {
    List<String> permissions(Long userId);
    List<String> getRoles(Long userId);
}
