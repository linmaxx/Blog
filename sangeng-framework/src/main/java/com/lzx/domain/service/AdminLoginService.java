package com.lzx.domain.service;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
public interface AdminLoginService{
    ResponseResult login(User user);

    ResponseResult getInfo();

    ResponseResult getRouters();

    ResponseResult logout();
}
