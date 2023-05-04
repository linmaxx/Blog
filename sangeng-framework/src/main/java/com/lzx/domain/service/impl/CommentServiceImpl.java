package com.lzx.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.Comment;
import com.lzx.domain.enums.AppHttpCodeEnum;
import com.lzx.domain.mapper.CommentMapper;
import com.lzx.domain.service.CommentService;
import com.lzx.domain.service.UserService;
import com.lzx.domain.vo.CommentVo;
import com.lzx.domain.vo.PageVo;
import com.lzx.exception.SystemException;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-03-06 11:20:18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //对articleId进行判断,当是文章评论时才，比较articleId进行判断
        lambdaQueryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        lambdaQueryWrapper.eq(Comment::getRootId, SystemConstants.IS_ROOT_COMMENT);
        lambdaQueryWrapper.eq(Comment::getType,commentType);
        Page<Comment> page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper); //按页查询平均
        List<Comment> comments = page.getRecords();
        List<CommentVo> commentVos = toCommentList(comments);
        //考虑子评论 查询当前评论的articleId、rootId和当前评论一致的子评论
        setChildrenComment(commentVos);
        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }
    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        baseMapper.insert(comment);
        return ResponseResult.okResult();
    }

    public List<CommentVo> toCommentList(List<Comment> comments){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(comments, CommentVo.class);;
        for(CommentVo commentVo:commentVos){
            //根据getCreateBy 查询 这条评论的username
            String nickName=userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //根据getToCommentUserId 查询 这条评论的回复的username
            if(commentVo.getToCommentUserId()!=-1){ //只有子评论才进行补全
                String tonickName=userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(tonickName);
            }

        }
        return commentVos;
    }
    //查询根评论的子评论
    public void setChildrenComment(List<CommentVo> commentVos){
        for(CommentVo commentVo:commentVos){
            LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getRootId,commentVo.getId());
            lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
            List<Comment> comments = list(lambdaQueryWrapper);
            List<CommentVo> commentVos1 = toCommentList(comments);
            commentVo.setChildren(commentVos1);
        }
    }
}

