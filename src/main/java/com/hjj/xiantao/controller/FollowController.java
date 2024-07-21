package com.hjj.xiantao.controller;

import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.request.follow.FollowRequest;
import com.hjj.xiantao.service.FollowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/follow")
public class FollowController {

    @Resource
    private FollowService followService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addFollow(@RequestBody FollowRequest followRequest, HttpServletRequest request) {
        if (followRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = followService.addFollow(followRequest, request);
        return ResultUtils.success(b);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFollow(@RequestBody FollowRequest followRequest, HttpServletRequest request) {
        if (followRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = followService.deleteFollow(followRequest, request);
        return ResultUtils.success(b);
    }
}
