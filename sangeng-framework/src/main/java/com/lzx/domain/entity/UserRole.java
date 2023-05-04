package com.lzx.domain.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 用户和角色关联表(UserRole)表实体类
 *
 * @author makejava
 * @since 2023-03-11 16:24:15
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_role")  //因为实体类名和表名，不一致，所以必须指定表名
public class UserRole {
    //用户ID
    private Long userId;
    //角色ID
    private Long roleId;

}

