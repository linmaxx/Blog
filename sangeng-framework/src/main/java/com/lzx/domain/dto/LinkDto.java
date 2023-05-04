package com.lzx.domain.dto;

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
@AllArgsConstructor
public class LinkDto {

    private Long id;
    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;

    private String status;

}
