package com.hjj.xiantao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.domain.Post;
import com.hjj.xiantao.model.domain.PostThumb;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.request.posthumb.PostThumbRequest;
import com.hjj.xiantao.service.PostService;
import com.hjj.xiantao.service.PostThumbService;
import com.hjj.xiantao.mapper.PostThumbMapper;
import com.hjj.xiantao.service.UserService;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 何佳骏
* @description 针对表【post_thumb(题目提交表)】的数据库操作Service实现
* @createDate 2024-05-31 12:14:49
*/
@Service
@Slf4j
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
    implements PostThumbService {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean addPostThumb(PostThumbRequest postThumbRequest, HttpServletRequest request) {
        Long postId = postThumbRequest.getPostId();
        Boolean isThumb = postThumbRequest.getIsThumb();
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 1.校验参数
        if (postId == null || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子不存在");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (isThumb == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "点赞失败");
        }
        if (isThumb) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您已点赞");
        }
        // 2.查询点赞帖子是否存在
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "帖子不存在");
        }
        // 3.将帖子表的点赞数 +1
        UpdateWrapper<Post> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("thumbNum + 1");
        updateWrapper.eq("id", postId);
        boolean update = postService.update(updateWrapper);
        if (!update) {
            log.error("{}用户点赞{}帖子的点赞数增加失败", userId, postId);
        }
        // 4.向帖子点赞表插入记录
        PostThumb postThumb = new PostThumb();
        postThumb.setPostId(postId);
        postThumb.setUserId(userId);
        boolean save = this.save(postThumb);
        if (!save) {
            log.error("{}用户点赞{}帖子的点赞记录插入失败", userId, postId);
        }
        return update && save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean deletePostThumb(PostThumbRequest postThumbRequest, HttpServletRequest request) {
        Long postId = postThumbRequest.getPostId();
        Boolean isThumb = postThumbRequest.getIsThumb();
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 1.检验参数
        if (postId == null || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子不存在");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (isThumb == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "取消点赞失败");
        }
        if (!isThumb) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您还未点赞");
        }
        // 2.查询取消点赞的帖子是否存在
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子不存在");
        }
        // 3.将帖子表的点赞数 -1
        UpdateWrapper<Post> postThumbUpdateWrapper = new UpdateWrapper<>();
        postThumbUpdateWrapper.setSql("thumbNum - 1");
        postThumbUpdateWrapper.eq("id", postId);
        boolean update = postService.update(postThumbUpdateWrapper);
        if (!update) {
            log.error("{}用户点赞{}帖子的点赞数减少失败", userId, postId);
        }
        // 4.向帖子点赞表删除记录
        QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
        postThumbQueryWrapper.eq("userId", userId);
        postThumbQueryWrapper.eq("postId", postId);
        boolean remove = this.remove(postThumbQueryWrapper);
        if (!remove) {
            log.error("{}用户点赞{}帖子的点赞记录删除失败", userId, postId);
        }
        return update && remove;
    }
}




