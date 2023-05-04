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
public class AddArticleDto {
    private String title;
    private String thumbnail;
    private String isTop;
    private String isComment;
    private String content;
    private List<Long> tags;
    private Long categoryId;
    private String summary;
    private String status;

}
