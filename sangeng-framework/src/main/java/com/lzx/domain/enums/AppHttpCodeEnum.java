package com.lzx.domain.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    REQUIRE_PASSWORD(508, "必需填写密码"),
    REQUIRE_NICKNAME(509, "必需填写昵称"),
    REQUIRE_EMAIL(510, "必需填写邮箱"),
    REPEAT_USERNAME(510, "用户名重复"),
    REPEAT_NICKNAME(510, "昵称重复"),
    REPEAT_EMAIL(510, "邮箱重复"),
    TAGNAME_NULL(511, "标签名为空"),
    CONTENT_NOT_NULL(506,"评论内容不能为空"),
    FILE_TYPE_ERROR(507,"文件类型不正确"),
    HAVE_SON_MENU(500,"存在子菜单不允许删除"),
    REPEAT_MENU(500,"修改菜单'写博文'失败，上级菜单不能选择自己"),
    REPEAT_PHONE(500,"用户电话重复"),
    NO_PERMIT_USER(500,"当前用户已被停用，请咨询管理员"),
    LOGIN_ERROR(505,"用户名或密码错误");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
