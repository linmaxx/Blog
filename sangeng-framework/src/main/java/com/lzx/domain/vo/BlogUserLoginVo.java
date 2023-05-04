package com.lzx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
public class BlogUserLoginVo {
    private String token;
    private UserVo userInfo;
    public BlogUserLoginVo(String token,UserVo userInfo){
        this.token=token;
        this.userInfo=userInfo;
    }
}
