package com.lzx.constants;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
public class SystemConstants {
    /*文章是草稿*/
    public static final int ARTICLE_STATUS_DRAFT=1;
    /*文章是正常状态*/
    public static final int ARTICLE_STATUS_NORMAL=0;
    //状态0:正常,1禁用
    /*分类状态是正常*/
    public static final String CATEGORY_STATUS_NORMAL="0";
    /*分类状态是禁用*/
    public static final String CATEGORY_STATUS_FORBIDDEN="1";
    //审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    public static final String LINK_STATUS_PASS="0";
    public static final String LINK_STATUS_FAIL="1";
    public static final String LINK_STATUS_NOT_APPROVER="2";
    // -1 表示评论为根评论
    public static final String IS_ROOT_COMMENT="-1";
    // 菜单类型 M表示目录
    public static final String MENU_TYPE_MULU="M";
    // 菜单类型 C表示菜单
    public static final String MENU_TYPE_MENU="C";
    // 菜单类型 F表示按钮
    public static final String MENU_TYPE_BUTTON="F";
    // 0 表示菜单状态正常
    public static final String MENU_STATUS_NORMAL="0";
    // 1 表示菜单状态停用
    public static final String MENU_STATUS_STOP="1";
    // 0 表示评论为文章评论
    public static final String ARTICLE_COMMENT="0";
    // 1 表示评论为友链评论
    public static final String FRIENDLINK_COMMENT="1";
    // 0 表示普通用户
    public static final String NORMAL_USER="0";
    // 1 表示后台用户
    public static final String ADMIN_USER="1";
    // 1 表示逻辑删除
    public static final String DEL_FLAG="1";
    // 0 表示角色状态正常
    public static final Object NORMAL_ROLE ="0" ;
}
