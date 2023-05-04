package com.lzx.domain.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 文章标签关联表(SgArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-03-10 14:34:13
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_article_tag")  //因为实体类名和表名，不一致，所以必须指定表名
public class ArticleTag {
    //文章id
    private Long articleId;
    //标签id
    private Long tagId;

}

