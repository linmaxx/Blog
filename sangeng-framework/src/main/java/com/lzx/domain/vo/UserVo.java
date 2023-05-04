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
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    //主键
    private Long id;

    private String userName;
    //昵称
    private String nickName;

    //邮箱
    private String email;

    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;

}
