package com.hjj.xiantao.controller;

import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.request.DeleteRequest;
import com.hjj.xiantao.model.request.post.PostAddRequest;
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

    @GetMapping("/recommend")
    public BaseResponse<List<PostVO>> recommendPosts(long pageNum, long pageSize, HttpServletRequest request) {
        if (pageNum < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<PostVO> postVOList = postService.recommendPosts(pageNum, pageSize, request);
        return ResultUtils.success(postVOList);
    }
}
