package com.hjj.xiantao.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.mapper.PostFavourMapper;
import com.hjj.xiantao.model.domain.*;
import com.hjj.xiantao.model.request.posthumb.PostThumbRequest;
import com.hjj.xiantao.model.vo.PostVO;
import com.hjj.xiantao.model.vo.UserVO;
import com.hjj.xiantao.service.PostImageService;
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
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;

    @Resource
    private PostImageService postImageService;

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
        updateWrapper.setSql("thumbNum = thumbNum + 1");
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
        postThumbUpdateWrapper.setSql("thumbNum = thumbNum - 1");
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

    @Override
    public List<PostVO> listMyPostThumb(long pageNum, long pageSize, HttpServletRequest request) {
        if (pageNum < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<PostThumb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<PostThumb> postThumbList = this.list(queryWrapper);
        List<Long> postIdList = postThumbList.stream().map(PostThumb::getPostId).collect(Collectors.toList());
        List<Post> postList = postService.listByIds(postIdList);
        List<PostVO> postVOList = postList.stream().map(post -> {
            // 设置 PostVO
            PostVO postVO = new PostVO();
            Long postId = post.getId();
            postVO.setId(postId);
            postVO.setTitle(post.getTitle());
            postVO.setContent(post.getContent());
            postVO.setTags(post.getTags());
            postVO.setPrice(post.getPrice());

            // 构建查询条件
            QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
            QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>();
            QueryWrapper<PostImage> postImageQueryWrapper = new QueryWrapper<>();

            postThumbQueryWrapper.eq("postId", postId);
            postFavourQueryWrapper.eq("postId", postId);
            postImageQueryWrapper.eq("postId", postId);
            // 查询帖子相关的点赞、收藏、图片
            Long thumbNum = postThumbMapper.selectCount(postThumbQueryWrapper);
            Long favourNum = postFavourMapper.selectCount(postFavourQueryWrapper);
            List<PostImage> postImageList = postImageService.list(postImageQueryWrapper);
            postVO.setThumbNum(thumbNum);
            postVO.setFavourNum(favourNum);
            postVO.setImages(JSONUtil.toJsonStr(postImageList));

            // 设置帖子的创建者（UserVO）
            User user = userService.getById(post.getUserId());
            postVO.setUserVO(UserVO.userToUserVO(user));
            return postVO;
        }).collect(Collectors.toList());
        return postVOList;
    }
}




