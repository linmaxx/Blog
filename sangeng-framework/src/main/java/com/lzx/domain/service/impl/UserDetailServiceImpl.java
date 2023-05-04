package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.entity.LoginUser;
import com.lzx.domain.entity.User;
import com.lzx.domain.mapper.AdminGetInfoMapper;
import com.lzx.domain.mapper.UserMapper;
import com.lzx.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminGetInfoMapper adminGetInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        //没查到，抛出异常
        if(user == null){
            throw new RuntimeException("用户ss名或密码错误");
        }
        if(user.getType().equals(SystemConstants.ADMIN_USER)){  //只有后台用户才需要权限控制
            List<String> permissions = adminGetInfoMapper.permissions(user.getId());
            return new LoginUser(user,permissions);
        }
        return new LoginUser(user,null);
    }
}
