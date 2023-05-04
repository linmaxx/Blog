package com.lzx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo {
    //登录用户的权限
    private List<String> permissions;
    //用户的角色
    private List<String> roles;
    //用户信息
    private UserVo user;
}
