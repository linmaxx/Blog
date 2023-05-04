package com.lzx.domain.service;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;


public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
