package com.lzx.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddRoleDto;
import com.lzx.domain.dto.ChangeRoleStatus;
import com.lzx.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-03-11 10:26:01
 */
public interface RoleService extends IService<Role> {

    ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(ChangeRoleStatus changeRoleStatus);

    ResponseResult treeselect();

    ResponseResult addRole(AddRoleDto roleDto);

    ResponseResult getRoleDetail(Long id);

    ResponseResult getRoleMenuTree(Long id);

    ResponseResult updateRole(AddRoleDto roleDto);

    ResponseResult deleteRole(List<Long> ids);

    ResponseResult listAllRole();
}

