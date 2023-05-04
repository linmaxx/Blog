package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AdminAddUserDto;
import com.lzx.domain.dto.AdminUpdateUserDto;
import com.lzx.domain.entity.*;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.mapper.RoleMapper;
import com.lzx.domain.mapper.UserMapper;
import com.lzx.domain.mapper.UserRoleMapper;
import com.lzx.domain.service.UserService;
import com.lzx.domain.vo.*;
import com.lzx.exception.SystemException;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.SecurityUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-03 16:19:46
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        return ResponseResult.okResult(userVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getId,user.getId());
        lambdaUpdateWrapper.set(User::getAvatar, user.getAvatar());
        lambdaUpdateWrapper.set(User::getEmail,user.getEmail());
        lambdaUpdateWrapper.set(User::getNickName, user.getNickName());
        lambdaUpdateWrapper.set(User::getSex, user.getSex());
        update(lambdaUpdateWrapper); //update 会自动将非空的值修改
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //要求用户名，密码，昵称，邮箱都不能为空
        if(user.getNickName() == null ){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_NICKNAME);
        }
        if(user.getUserName() == null ){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if(user.getPassword() == null ){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_PASSWORD);
        }
        if(user.getEmail() == null ){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_EMAIL);
        }
        //判断数据库是否为空
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REPEAT_USERNAME);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.REPEAT_NICKNAME);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.REPEAT_EMAIL);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        Page<User> page=new Page(pageNum,pageSize);
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.hasText(userName),User::getUserName,userName);
        lambdaQueryWrapper.eq(Strings.hasText(phonenumber),User::getPhonenumber,phonenumber);
        lambdaQueryWrapper.eq(Strings.hasText(status),User::getStatus,status);
        page(page, lambdaQueryWrapper);
        List<AdminUserVo> adminUserVos = BeanCopyUtils.copyBeanList(page.getRecords(), AdminUserVo.class);
        return ResponseResult.okResult(new PageVo(adminUserVos,page.getTotal()));
    }

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public ResponseResult addUser(AdminAddUserDto userDto) {
        //第一步 ：将用户基本信息加入user表
        //对用户密码进行加密存储
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        //用户名不能为空
        if(user.getUserName() == null ){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        //判断数据库是否有重复的用户名、手机号、邮箱
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REPEAT_USERNAME); //用户名重复
        }
        if(phonenumberExist(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.REPEAT_PHONE); //手机号重复
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.REPEAT_EMAIL); //邮箱重复
        }
        save(user);
        //第二步：将用户id 和 roleId进行绑定 存入UserRole
        List<Long> roleIds = userDto.getRoleIds();
        roleIds.stream()
                .forEach(roleId->userRoleMapper.insert(new UserRole(user.getId(),roleId)));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(List<Long> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(Long userId, String status) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(User::getStatus, status);
        lambdaUpdateWrapper.eq(User::getId, userId);
        update(lambdaUpdateWrapper);
        return ResponseResult.okResult();
    }

    @Autowired
    private RoleMapper roleMapper;
    @Override
    public ResponseResult getUserDetails(Long id) {
        //获取用户信息
        User user = getById(id);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        //获取用户所关联的角色id列表
        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(UserRole::getRoleId);
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        List<Long> roleIds = userRoles.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        //获取所有角色的列表
        List<Role> roles = roleMapper.selectList(null);
        List<AdminRoleVo> adminRoleVos = BeanCopyUtils.copyBeanList(roles, AdminRoleVo.class);

        return ResponseResult.okResult(new AdminUpdateGetUserVo(userVo,roleIds,adminRoleVos));
    }

    @Override
    public ResponseResult updateUser(AdminUpdateUserDto userDto) {
        //将用户信息插入user表中
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        updateById(user);
        //删除该角色原有的roleId
        LambdaUpdateWrapper<UserRole> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleMapper.delete(lambdaUpdateWrapper);
        //将 user 和 role  的对应关系添加到 role_menu表中
        List<Long> roleIds = userDto.getRoleIds();
        roleIds.stream()
                .forEach(roleId->userRoleMapper.insert(new UserRole(user.getId(),roleId)));
        return ResponseResult.okResult();
    }

    public boolean exist(SFunction<User,?> column,String key){
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(column, key);
        List<User> one = list(lambdaQueryWrapper); //为了测试这里写成list
        if(!one.isEmpty()){
            return true;
        }
        return false;
    }
    public boolean userNameExist(String userName){
        return exist(User::getUserName,userName);
    }
    public boolean nickNameExist(String nickName){
        return exist(User::getNickName,nickName);
    }
    public boolean phonenumberExist(String phonenumber){
        return exist(User::getPhonenumber,phonenumber);
    }
    public boolean emailExist(String email){
        return exist(User::getEmail,email);
    }
}

