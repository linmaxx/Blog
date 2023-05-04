package com.lzx.admin.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AdminAddUserDto;
import com.lzx.domain.dto.AdminUpdateUserDto;
import com.lzx.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.adminList(pageNum,pageSize,userName,phonenumber,status);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody AdminAddUserDto userDto){
        return userService.addUser(userDto);
    }
    @DeleteMapping("/{ids}")
    public ResponseResult deleteUser(@PathVariable("ids") List<Long> ids){
        return userService.deleteUser(ids);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Map<String,String> map){
        return userService.changeStatus(Long.valueOf(map.get("userId")), map.get("status"));
    }
    /**
    *@author linmaxxx
    *@Description 修改用户时，回显用户信息
    *@Date 16:56 2023/3/11
    **/
    @GetMapping("/{id}")
    public ResponseResult getUserDetails(@PathVariable("id") Long id){
        return userService.getUserDetails(id);
    }
    /**
    *@author linmaxxx
    *@Description 更新用户信息接口
    *@Date 17:14 2023/3/11
    **/
    @PutMapping
    public ResponseResult updateUser(@RequestBody AdminUpdateUserDto userDto){
        return userService.updateUser(userDto);
    }


}
