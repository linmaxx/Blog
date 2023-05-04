package com.lzx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMenuBackVo {  //修改菜单时，需要回显的数据格式
    //菜单ID
    private Long id;
    //菜单名称
    private String menuName;

    //显示顺序
    private Integer orderNum;
    //路由地址
    private String path;

    //父菜单ID
    private Long parentId;
    //菜单类型（M目录 C菜单 F按钮）
    private String menuType;
    //菜单状态（0显示 1隐藏）
    private String visible;
    //菜单状态（0正常 1停用）
    private String status;

    //备注
    private String remark;
    //菜单图标
    private String icon;


}
