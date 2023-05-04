package com.lzx.blog.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.service.LoginService;
import com.lzx.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
public class BlogLoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要穿用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
