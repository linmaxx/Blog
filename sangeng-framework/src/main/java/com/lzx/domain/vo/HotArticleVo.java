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
public class HotArticleVo {  //一个接口对应一个ov类

    private Long id;

    //标题
    private String title;

    //访问量
    private Long viewCount;

}
