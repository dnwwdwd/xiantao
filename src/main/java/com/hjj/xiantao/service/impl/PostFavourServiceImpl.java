package com.hjj.xiantao.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.mapper.PostFavourMapper;
import com.hjj.xiantao.model.domain.Post;
import com.hjj.xiantao.model.domain.PostFavour;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.request.postfavour.PostFavourRequest;
import com.hjj.xiantao.service.PostFavourService;
import com.hjj.xiantao.service.PostService;
import com.hjj.xiantao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 何佳骏
* @description 针对表【post_favour(帖子收藏)】的数据库操作Service实现
* @createDate 2024-05-31 12:14:49
*/
@Service
@Slf4j
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
    implements PostFavourService {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean addPostFavour(PostFavourRequest postFavourRequest, HttpServletRequest request) {
        Long postId = postFavourRequest.getPostId();
        Boolean isFavour = postFavourRequest.getIsFavour();
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 1.校验参数
        if (postId == null || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子不存在");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (isFavour == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "收藏失败");
        }
        if (isFavour) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您已收藏");
        }
        // 2.判断收藏的帖子是否存在
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子不存在");
        }
        // 3.将帖子表的收藏数 +1
        UpdateWrapper<Post> postUpdateWrapper = new UpdateWrapper<>();
        postUpdateWrapper.setSql("favourNum + 1");
        postUpdateWrapper.eq("postId", postId);
        boolean update = postService.update(postUpdateWrapper);
        if (!update) {
            log.error("{}用户收藏{}帖子的收藏数增加失败", userId, postId);
        }
        // 4.向帖子收藏表插入记录
        PostFavour postFavour = new PostFavour();
        postFavour.setPostId(postId);
        postFavour.setUserId(userId);
        boolean save = this.save(postFavour);
        if (!save) {
            log.error("{}用户收藏{}帖子的点赞收藏插入失败", userId, postId);
        }
        return update && save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean deletePostFavour(PostFavourRequest postFavourRequest, HttpServletRequest request) {
        Long postId = postFavourRequest.getPostId();
        Boolean isFavour = postFavourRequest.getIsFavour();
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 1.校验参数
        if (postId == null || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子不存在");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (isFavour == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "取消收藏失败");
        }
        if (!isFavour) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您还未收藏");
        }
        // 2.判断收藏的帖子是否存在
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子不存在");
        }
        // 3.将帖子表的收藏数 -1
        UpdateWrapper<Post> postUpdateWrapper = new UpdateWrapper<>();
        postUpdateWrapper.setSql("favourNum - 1");
        postUpdateWrapper.eq("postId", postId);
        boolean update = postService.update(postUpdateWrapper);
        if (!update) {
            log.error("{}用户收藏{}帖子的收藏数减少失败", userId, postId);
        }
        // 4.向帖子收藏表插入记录
        PostFavour postFavour = new PostFavour();
        postFavour.setPostId(postId);
        postFavour.setUserId(userId);
        boolean save = this.save(postFavour);
        if (!save) {
            log.error("{}用户收藏{}帖子的点赞收藏删除失败", userId, postId);
        }
        return update && save;
    }

}




