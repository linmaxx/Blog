package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddRoleDto;
import com.lzx.domain.dto.ChangeRoleStatus;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/system")
public class RoleController {
    @Autowired
    private RoleService roleService;
    /**
    *@author linmaxxx
    *@Description 查询的是所有状态正常的角色
    *@Date 15:51 2023/3/11
    *@Param [pageNum, pageSize, roleName, status]
    *@return com.lzx.domain.ResponseResult
    **/
    @GetMapping("/role/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
    @GetMapping("/role/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String roleName,String status){  //后台查询角色列表
        return roleService.list(pageNum,pageSize,roleName,status);
    }
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    @PutMapping("/role/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatus changeRoleStatus){
        return roleService.changeStatus(changeRoleStatus);
    }
    @GetMapping("/menu/treeselect")
    public ResponseResult treeselect(){  //增加角色时，显示菜单树
        return roleService.treeselect();
    }
    @PostMapping("/role")
    public ResponseResult addRole(@RequestBody AddRoleDto roleDto){
        return roleService.addRole(roleDto);
    }
    /**
    *@author linmaxxx
    *@Description 修改角色中的第一步：回显角色的详细信息
    *@Date 13:57 2023/3/11
    *@Param [id]
    *@return com.lzx.domain.ResponseResult
    **/
    @GetMapping("/menu/roleMenuTreeselect/{id}")
    public ResponseResult getRoleMenuTree(@PathVariable("id") Long id){
        return roleService.getRoleMenuTree(id);
    }
    /**
    *@author linmaxxx
    *@Description 加载对应角色菜单列表树接口
    *@Date 14:03 2023/3/11
    *@Param [id]
    *@return com.lzx.domain.ResponseResult
    **/
    @GetMapping("/role/{id}")
    public ResponseResult getRoleDetail(@PathVariable("id") Long id){
        return roleService.getRoleDetail(id);
    }
    /**
    *@author linmaxxx
    *@Description 修改角色
    *@Date 14:39 2023/3/11
    *@Param []
    *@return com.lzx.domain.ResponseResult
    **/
    @PutMapping("/role")
    public ResponseResult updateRole(@RequestBody AddRoleDto roleDto){
        return roleService.updateRole(roleDto);
    }
    /**
    *@author linmaxxx
    *@Description 根据id删除角色
    *@Date 15:10 2023/3/11
    *@Param [id]
    *@return com.lzx.domain.ResponseResult
    **/
    @DeleteMapping("/role/{ids}")
    public ResponseResult deleteRole(@PathVariable("ids") List<Long> ids){
        return roleService.deleteRole(ids);
    }
}
