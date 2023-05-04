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
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateUserDto {
    private String email;
    private Long id;
    private String nickName;

    private String sex;
    private String status;
    private String userName;

    private List<Long> roleIds;

}
