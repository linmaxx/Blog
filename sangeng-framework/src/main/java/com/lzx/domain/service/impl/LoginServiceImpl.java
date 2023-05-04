package com.lzx.domain.service.impl;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.LoginUser;
import com.lzx.domain.entity.User;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.vo.BlogUserLoginVo;
import com.lzx.domain.service.LoginService;
import com.lzx.domain.vo.UserVo;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.JwtUtil;
import com.lzx.utils.RedisCache;
import com.lzx.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if(authentication==null){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId, loginUser);
        //把token和userinfo封装返回给响应给前端
        UserVo userInfo = BeanCopyUtils.copyBean(loginUser.getUser(), UserVo.class);
        BlogUserLoginVo blogUserLoginVo=new BlogUserLoginVo(jwt,userInfo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        redisCache.deleteObject("login:"+loginUser.getUser().getId());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS.getCode(), "操作成功");
    }
}
