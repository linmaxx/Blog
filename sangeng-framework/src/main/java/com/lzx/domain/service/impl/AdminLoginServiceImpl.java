package com.lzx.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.LoginUser;
import com.lzx.domain.entity.Menu;
import com.lzx.domain.entity.User;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.mapper.AdminGetInfoMapper;

import com.lzx.domain.mapper.MenuMapper;
import com.lzx.domain.service.AdminLoginService;
import com.lzx.domain.service.MenuService;
import com.lzx.domain.service.UserService;
import com.lzx.domain.vo.MenuVo;
import com.lzx.domain.vo.ReturnMenuVo;
import com.lzx.domain.vo.UserInfoVo;
import com.lzx.domain.vo.UserVo;
import com.lzx.exception.SystemException;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.JwtUtil;
import com.lzx.utils.RedisCache;
import com.lzx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminGetInfoMapper adminGetInfoMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(authenticate == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR); //密码校验是自动完成的
        }
        //认证通过
        LoginUser loginUser= (LoginUser)authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        if("1".equals(loginUser.getUser().getStatus())){ //判断当前用户是否停用,停用则禁止登陆
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_PERMIT_USER);
        }
        //将用户信息存放到 redis
        redisCache.setCacheObject("adminLogin:"+userId, loginUser);
        //通过userId 生成 jwt 返回给前端
        String jwt = JwtUtil.createJWT(userId.toString());
        Map<String,String> map=new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult getInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = userService.getById(userId);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        if(userId == 1){  //说明是管理员
            List<String> roles=new ArrayList<>();
            roles.add("admin");
            List<String> permissons = menuMapper.getPermissons();
            UserInfoVo userInfoVo=new UserInfoVo(permissons,roles,userVo);
            return ResponseResult.okResult(userInfoVo);
        }
        List<String> permissions = adminGetInfoMapper.permissions(userId);
        List<String> roles = adminGetInfoMapper.getRoles(userId);
        UserInfoVo userInfoVo=new UserInfoVo(permissions,roles,userVo);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus=null;
        if(userId == 1){
            LambdaQueryWrapper<Menu> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.orderByAsc(Menu::getOrderNum);
            lambdaQueryWrapper.in(Menu::getMenuType, SystemConstants.MENU_TYPE_MENU,SystemConstants.MENU_TYPE_MULU);
            lambdaQueryWrapper.eq(Menu::getStatus,SystemConstants.MENU_STATUS_NORMAL);
            menus = menuMapper.selectList(lambdaQueryWrapper);
        }else {
            menus= menuMapper.getMenus(userId);
        }
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        List<MenuVo> res = setMenuVoChildren(menuVos);
        return ResponseResult.okResult(new ReturnMenuVo(res));
    }

    @Override
    public ResponseResult logout() {
        //获取此时用户登录的 id
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("adminLogin:"+userId);
        return ResponseResult.okResult();
    }

    public  List<MenuVo>  setMenuVoChildren(List<MenuVo> menuVos){
        //获取根菜单 然后去找他们的子菜单设置到children属性中
        List<MenuVo> res = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(0L))
                .collect(Collectors.toList());
        res.stream()
                .forEach(menuVo -> menuVo.setChildren(getChildren(menuVos,menuVo.getId())));
        return res;

    }
    public List<MenuVo> getChildren(List<MenuVo> menuVos,Long parentId){
        List<MenuVo> childrens = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId) )
                .collect(Collectors.toList());
        if(childrens.isEmpty()){
            return new ArrayList<>();
        }else {
            childrens.stream()
                    .forEach(children->children.setChildren(getChildren(menuVos, children.getId())));
            return childrens;
        }
    }
}
