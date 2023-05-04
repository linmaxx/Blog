package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;
import com.lzx.domain.service.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return adminLoginService.login(user);
    }
    @GetMapping("getInfo")
    public ResponseResult getInfo(){
        return adminLoginService.getInfo();
    }
    @GetMapping("getRouters")
    public ResponseResult getRouters(){
        return adminLoginService.getRouters();
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }

}
