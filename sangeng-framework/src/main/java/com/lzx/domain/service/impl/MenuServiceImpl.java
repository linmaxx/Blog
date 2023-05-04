package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.Menu;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.mapper.MenuMapper;
import com.lzx.domain.service.MenuService;
import com.lzx.domain.vo.AdminMenuBackVo;
import com.lzx.domain.vo.AdminMenuVo;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-09 16:06:31
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    //后台 查询所有菜单
    @Override
    public ResponseResult adminMenuList(String status, String menuName) {
        LambdaUpdateWrapper<Menu> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.like(StringUtils.hasText(status), Menu::getStatus, status).
                or().like(StringUtils.hasText(menuName), Menu::getMenuName, menuName );
        lambdaUpdateWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(lambdaUpdateWrapper);
        List<AdminMenuVo> adminMenuVos = BeanCopyUtils.copyBeanList(menus, AdminMenuVo.class);
        return ResponseResult.okResult(adminMenuVos);
    }

    //后台增加菜单
    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }
    //后台 修改菜单中的回显代码
    @Override
    public ResponseResult getMenuDetail(long id) {
        Menu menu = getById(id);
        AdminMenuBackVo adminMenuBackVo = BeanCopyUtils.copyBean(menu, AdminMenuBackVo.class);
        return ResponseResult.okResult(adminMenuBackVo);
    }

    @Override
    public ResponseResult adminUpdateMenu(Menu menu) {
        Long parentId = menu.getParentId();
        if(menu.getId().equals(parentId)){
            return ResponseResult.errorResult(AppHttpCodeEnum.REPEAT_MENU);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminDeleteMenu(long menuId) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> list = list(lambdaQueryWrapper);
        if(!list.isEmpty()){ //要删除的菜单有子菜单则
            return ResponseResult.errorResult(AppHttpCodeEnum.HAVE_SON_MENU);
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }
}

