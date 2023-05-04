package com.lzx.domain.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 角色和菜单关联表(RoleMenu)表实体类
 *
 * @author makejava
 * @since 2023-03-11 13:39:48
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")  //因为实体类名和表名，不一致，所以必须指定表名
public class RoleMenu {
    //角色ID
    private Long roleId;
    //菜单ID
    private Long menuId;

}

