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
public class ArticleListVo {
    private Long id;
    //标题
    private String title;
    //缩略图
    private String thumbnail;
    //文章摘要
    private String summary;
    //访问量
    private Long viewCount;
    //分类名
    private String name;

    private Date createTime;
}
