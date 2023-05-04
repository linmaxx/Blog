package com.lzx.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.Menu;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-03-09 16:06:31
 */
public interface MenuService extends IService<Menu> {

    ResponseResult adminMenuList(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenuDetail(long id);

    ResponseResult adminUpdateMenu(Menu menu);

    ResponseResult adminDeleteMenu(long menuId);
}

