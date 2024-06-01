package com.hjj.xiantao.controller;

import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.request.postfavour.PostFavourRequest;
import com.hjj.xiantao.service.PostFavourService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post_favour")
public class PostFavourController {

    @Resource
    private PostFavourService postFavourService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addPostFavour(@RequestBody PostFavourRequest postFavourRequest,
                                               HttpServletRequest request) {
        if (postFavourRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = postFavourService.addPostFavour(postFavourRequest, request);
        return ResultUtils.success(b);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePostFavour(@RequestBody PostFavourRequest postFavourRequest,
                                               HttpServletRequest request) {
        if (postFavourRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = postFavourService.deletePostFavour(postFavourRequest, request);
        return ResultUtils.success(b);
    }
}
