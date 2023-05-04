package com.lzx.domain.dto;

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
public class AdminAddUserDto {
    private String userName;
    private String nickName;
    private String password;
    private String phonenumber;

    private String email;
    private String sex;
    private String status;
    private List<Long> roleIds;
}
