package com.lzx.blog.controller;

import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddCommentDto;
import com.lzx.domain.entity.Comment;
import com.lzx.domain.service.CommentService;
import com.lzx.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/comment")
@Api(tags="评论",description = "评论相关的接口")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId, pageNum, pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto comment){
        Comment comment1 = BeanCopyUtils.copyBean(comment, Comment.class);
        return commentService.addComment(comment1);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.FRIENDLINK_COMMENT,null,pageNum, pageSize);
    }
}
