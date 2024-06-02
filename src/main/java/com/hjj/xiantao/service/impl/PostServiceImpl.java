package com.hjj.xiantao.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.mapper.PostFavourMapper;
import com.hjj.xiantao.mapper.PostImageMapper;
import com.hjj.xiantao.mapper.PostMapper;
import com.hjj.xiantao.mapper.PostThumbMapper;
import com.hjj.xiantao.model.domain.*;
import com.hjj.xiantao.model.request.DeleteRequest;
import com.hjj.xiantao.model.request.post.PostAddRequest;
import com.hjj.xiantao.model.request.post.PostQueryRequest;
import com.hjj.xiantao.model.vo.PostVO;
import com.hjj.xiantao.model.vo.UserVO;
import com.hjj.xiantao.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 何佳骏
 * @description 针对表【post(帖子)】的数据库操作Service实现
 * @createDate 2024-05-31 12:14:49
 */
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private PostThumbService postThumbService;

    @Resource
    @Lazy
    private PostFavourService postFavourService;

    @Resource
    private PostImageService postImageService;

    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;

    @Resource
    private PostMapper postMapper;


    @Override
    public Long addPost(PostAddRequest postAddRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String title = postAddRequest.getTitle();
        String content = postAddRequest.getContent();
        Integer price = postAddRequest.getPrice();
        List<String> tags = postAddRequest.getTags();
        Long userId = loginUser.getId();
        // 1.校验参数
        if (StrUtil.hasBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帖子标题或者内容不可为空");
        }
        if (CollectionUtil.isEmpty(tags)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请至少选择一个标签");
        }
        if (price == null || price <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "价格必须大于 0");
        }
        // 2.对 tags 集合进行拼接
        StringBuffer tagsStringBuffer = new StringBuffer();
        tagsStringBuffer.append("[");
        for (int i = 0; i < tags.size(); i++) {
            tagsStringBuffer.append('"').append(tags.get(i)).append('"');
            if (i < tags.size() - 1) {
                tagsStringBuffer.append(',');
            }
        }
        tagsStringBuffer.append("]");
        // 3.插入数据库
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setTags(tagsStringBuffer.toString());
        post.setUserId(userId);
        boolean save = this.save(post);
        if (!save) {
            log.info("帖子插入数据库失败");
        }
        return post.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePost(DeleteRequest deleteRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long id = deleteRequest.getId();
        // 1.校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.查找帖子相关的点赞，收藏和图片
        QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
        QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>();
        QueryWrapper<PostImage> postImageQueryWrapper = new QueryWrapper<>();
        postThumbQueryWrapper.eq("postId", id);
        postFavourQueryWrapper.eq("postId", id);
        postImageQueryWrapper.eq("postId", id);
        List<Long> postThumbIdList = postThumbService.list(postThumbQueryWrapper)
                .stream().map(PostThumb::getId)
                .collect(Collectors.toList());
        List<Long> postFavourIdList = postFavourService.list(postFavourQueryWrapper)
                .stream().map(PostFavour::getId)
                .collect(Collectors.toList());
        List<Long> postImageIdList = postImageService.list(postImageQueryWrapper)
                .stream().map(PostImage::getId)
                .collect(Collectors.toList());
        boolean b1 = postThumbService.removeByIds(postThumbIdList, CollectionUtil.isNotEmpty(postThumbIdList));
        boolean b2 = postFavourService.removeByIds(postFavourIdList, CollectionUtil.isNotEmpty(postFavourIdList));
        boolean b3 = postImageService.removeByIds(postImageIdList, CollectionUtil.isNotEmpty(postImageIdList));
        if (!b1) {
            log.info("{}帖子相关点赞删除失败", id);
        }
        if (!b2) {
            log.info("{}帖子相关收藏删除失败", id);
        }
        if (!b3) {
            log.info("{}帖子相关图片删除失败", id);
        }
        return b1 && b2 && b3;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePostByMySelf(DeleteRequest deleteRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        // 1.检验参数
        Long postId = deleteRequest.getId();
        if (postId == null || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.判断删除帖子是否为自己创建
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "删除的帖子不存在");
        }
        if (post.getUserId() == null || post.getUserId() != loginUser.getId()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不可下架非自己发布的宝贝");
        }
        // 3.查找帖子相关的点赞，收藏和图片
        QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
        QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>();
        postThumbQueryWrapper.eq("postId", postId);
        postFavourQueryWrapper.eq("postId", postId);
        List<Long> postThumbIdList = postThumbService.list(postThumbQueryWrapper)
                .stream().map(PostThumb::getId)
                .collect(Collectors.toList());
        List<Long> postFavourIdList = postFavourService.list(postFavourQueryWrapper)
                .stream().map(PostFavour::getId)
                .collect(Collectors.toList());
        boolean b1 = postThumbService.removeByIds(postThumbIdList, CollectionUtil.isNotEmpty(postThumbIdList));
        boolean b2 = postFavourService.removeByIds(postFavourIdList, CollectionUtil.isNotEmpty(postFavourIdList));
        if (!b1) {
            log.info("{}帖子相关点赞删除失败", postId);
        }
        if (!b2) {
            log.info("{}帖子相关收藏删除失败", postId);
        }
        return b1 && b2;
    }

    @Override
    public List<PostVO> recommendPosts(long pageNum, long pageSize, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Page<Post> postPage = this.page(new Page<>(pageNum, pageSize));
        List<Post> postList = postPage.getRecords();
        if (CollectionUtil.isEmpty(postList)) {
            return new ArrayList<>();
        }
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

    @Override
    public List<PostVO> searchPost(PostQueryRequest postQueryRequest, HttpServletRequest request) {
        long pageSize = postQueryRequest.getPageSize();
        long pageNum = postQueryRequest.getPageNum();
        Page<Post> postPage = this.page(new Page<>(pageNum, pageSize), getQueryWrapper(postQueryRequest));
        List<PostVO> postVOList = postPage.getRecords().stream().map(post -> {
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

    @Override
    public List<PostVO> listMyPost(long pageNum, long pageSize, HttpServletRequest request) {
        if (pageNum < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<Post> postList = this.page(new Page<>(pageNum, pageSize)).getRecords();
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

    private QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest) {
        String searchParam = postQueryRequest.getSearchParam();
        Integer minPrice = postQueryRequest.getMinPrice();
        Integer maxPrice = postQueryRequest.getMaxPrice();
        String orderName = postQueryRequest.getOrderName();
        String asc = postQueryRequest.getAsc();

        boolean b = (minPrice != null && maxPrice != null) && (maxPrice > 0 && minPrice <= maxPrice);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(StrUtil.isNotBlank(searchParam), qw1 -> qw1.like("content", searchParam)
                .or(qw2 -> qw2.like("title", searchParam)));
        queryWrapper.between(b, "price", minPrice, maxPrice);
        queryWrapper.orderBy(StrUtil.isNotBlank(orderName), "asc".equals(asc), orderName);
        return queryWrapper;
    }
}
