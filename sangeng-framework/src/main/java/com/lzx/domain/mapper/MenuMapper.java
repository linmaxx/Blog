package com.lzx.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-09 16:06:30
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> getPermissons();
    List<Menu> getMenus(Long userId);
}
