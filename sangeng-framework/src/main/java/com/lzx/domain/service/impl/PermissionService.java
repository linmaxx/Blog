package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.lzx.exception.SystemException;
import com.lzx.utils.SecurityUtils;
import io.netty.util.internal.ObjectUtil;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        if(SecurityUtils.isAdmin()){  //如果是超级用户 直接通过
            return true;
        }
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
