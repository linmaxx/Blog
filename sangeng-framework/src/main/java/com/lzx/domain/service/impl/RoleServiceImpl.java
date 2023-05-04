package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddRoleDto;
import com.lzx.domain.dto.ChangeRoleStatus;
import com.lzx.domain.entity.Menu;
import com.lzx.domain.entity.Role;
import com.lzx.domain.entity.RoleMenu;
import com.lzx.domain.mapper.MenuMapper;
import com.lzx.domain.mapper.RoleMapper;
import com.lzx.domain.mapper.RoleMenuMapper;
import com.lzx.domain.service.RoleService;
import com.lzx.domain.vo.*;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-03-11 10:26:01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    // 显示所有角色信息，并且可以角色名称进行模糊查询、针对状态进行查询、按照role_sort进行升序排列
    @Override
    public ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        lambdaQueryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<Role> records = page.getRecords();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(records, RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos,page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(ChangeRoleStatus changeRoleStatus) {
        long roleId = changeRoleStatus.getRoleId();
        String status = changeRoleStatus.getStatus();
        LambdaUpdateWrapper<Role> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Role::getStatus, status);
        lambdaUpdateWrapper.eq(Role::getId,roleId);
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeselect() {
        //先查询菜单表获取所有行结果
        List<Menu> menus = menuMapper.selectList(null);
        //为了解决前端字段不一致的情况 做一个中转
        List<TransferVo> transferVos = BeanCopyUtils.copyBeanList(menus, TransferVo.class);
        transferVos.stream()
                .forEach(transferVo -> transferVo.setLabel(transferVo.getMenuName()));
        List<AdminAddRoleTreeVo> adminAddRoleTreeVos = BeanCopyUtils.copyBeanList(transferVos, AdminAddRoleTreeVo.class);
        //得到根目录
        List<AdminAddRoleTreeVo> res = adminAddRoleTreeVos.stream()
                .filter(adminAddRoleTreeVo -> adminAddRoleTreeVo.getParentId().equals(0L) )
                .collect(Collectors.toList());
        //将根目录传入，在所有行结果中递归设置children
        res.stream()
                .forEach(menuVo -> menuVo.setChildren(getChildren(adminAddRoleTreeVos,menuVo.getId())));
        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult addRole(AddRoleDto roleDto) {
        //将角色信息插入role 表中，并得到自增主键 roleId
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);
        //将role  和 menu 的对应关系添加到 role_menu表中
        List<Long> menuIds = roleDto.getMenuIds();
        menuIds.stream()
                .forEach(menuId->roleMenuMapper.insert(new RoleMenu(role.getId(), menuId)));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleDetail(Long id) {
        Role role = getById(id);
        AdminUpdateGetRoleVo adminUpdateGetRoleVo = BeanCopyUtils.copyBean(role, AdminUpdateGetRoleVo.class);
        return ResponseResult.okResult(adminUpdateGetRoleVo);
    }

    @Override
    public ResponseResult getRoleMenuTree(Long id) {
        List<AdminAddRoleTreeVo> res=(List<AdminAddRoleTreeVo>)treeselect().getData();
        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(lambdaQueryWrapper);
        List<Long> menuIds = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        RoleMenuTressVo roleMenuTressVo=new RoleMenuTressVo(res, menuIds);
        return ResponseResult.okResult(roleMenuTressVo);
    }

    @Override
    public ResponseResult updateRole(AddRoleDto roleDto) {
        //将角色信息插入role 表中，并得到自增主键 roleId
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        updateById(role);
        //删除该角色原有的menuId
        LambdaUpdateWrapper<RoleMenu> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuMapper.delete(lambdaUpdateWrapper);
        //将role  和 menu 的对应关系添加到 role_menu表中
        List<Long> menuIds = roleDto.getMenuIds();
        menuIds.stream()
                .forEach(menuId->roleMenuMapper.insert(new RoleMenu(role.getId(), menuId)));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(List<Long> ids) {
        ids.stream()
                .forEach(id->
                {
                    //删除 role表和rolemenu中对应的角色信息
                    removeById(id);
                    //删除该角色原有的menuId
                    LambdaUpdateWrapper<RoleMenu> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(RoleMenu::getRoleId,id);
                    roleMenuMapper.delete(lambdaUpdateWrapper);
                });
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getStatus, SystemConstants.NORMAL_ROLE);
        List<Role> roles = list(lambdaQueryWrapper);
        List<AdminRoleVo> adminRoleVos = BeanCopyUtils.copyBeanList(roles, AdminRoleVo.class);
        return ResponseResult.okResult(adminRoleVos);
    }

    public List<AdminAddRoleTreeVo> getChildren(List<AdminAddRoleTreeVo> menuVos,Long parentId){
        List<AdminAddRoleTreeVo> childrens = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .collect(Collectors.toList());
        if(childrens.isEmpty()){
            return new ArrayList<>();
        }else {
            childrens.stream()
                    .forEach(children->children.setChildren(getChildren(menuVos, children.getId())));
            return childrens;
        }
    }

}

