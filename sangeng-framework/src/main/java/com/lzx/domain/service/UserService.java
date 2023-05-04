package com.lzx.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AdminAddUserDto;
import com.lzx.domain.dto.AdminUpdateUserDto;
import com.lzx.domain.entity.User;

import java.util.List;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-03-03 16:19:46
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult adminList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(AdminAddUserDto userDto);

    ResponseResult deleteUser(List<Long> ids);

    ResponseResult changeStatus(Long userId, String status);

    ResponseResult getUserDetails(Long id);

    ResponseResult updateUser(AdminUpdateUserDto userDto);
}

