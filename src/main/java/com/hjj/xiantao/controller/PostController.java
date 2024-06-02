package com.hjj.xiantao.controller;

import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.request.DeleteRequest;
import com.hjj.xiantao.model.request.post.PostAddRequest;
import com.hjj.xiantao.model.request.post.PostQueryRequest;
import com.hjj.xiantao.model.vo.PostVO;
import com.hjj.xiantao.service.PostService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;

    /**
     * 发布
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        if (postAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = postService.addPost(postAddRequest, request);
        return ResultUtils.success(id);
    }

    /**
     * 删除帖子（仅限管理员）
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePost(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean delete = postService.deletePost(deleteRequest, request);
        return ResultUtils.success(delete);
    }

    @PostMapping("/delete/my")
    public BaseResponse<Boolean> deletePostByMyself(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean delete = postService.deletePostByMySelf(deleteRequest, request);
        return ResultUtils.success(delete);
    }

    /**
     * 首页推荐帖子
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/recommend")
    public BaseResponse<List<PostVO>> recommendPosts(long pageNum, long pageSize, HttpServletRequest request) {
        if (pageNum < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<PostVO> postVOList = postService.recommendPosts(pageNum, pageSize, request);
        return ResultUtils.success(postVOList);
    }

    /**
     * 搜索帖子
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search")
    public BaseResponse<List<PostVO>> searchPost(@RequestBody PostQueryRequest postQueryRequest,
                                                 HttpServletRequest request) {
        if (postQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<PostVO> postVOList = postService.searchPost(postQueryRequest, request);
        return ResultUtils.success(postVOList);
    }

    /**
     * 查询我发布的帖子
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/list/my")
    public BaseResponse<List<PostVO>> listMyPost(long pageNum, long pageSize, HttpServletRequest request) {
        List<PostVO> postVOList = postService.listMyPost(pageNum, pageSize, request);
        return ResultUtils.success(postVOList);
    }
}