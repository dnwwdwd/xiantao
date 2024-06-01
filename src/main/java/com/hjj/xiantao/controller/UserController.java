package com.hjj.xiantao.controller;

import cn.hutool.core.util.StrUtil;
import com.hjj.xiantao.common.BaseResponse;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.common.ResultUtils;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.request.user.UserLoginRequest;
import com.hjj.xiantao.model.request.user.UserQueryRequest;
import com.hjj.xiantao.model.request.user.UserRegisterRequest;
import com.hjj.xiantao.model.vo.SafetyUser;
import com.hjj.xiantao.model.vo.UserVO;
import com.hjj.xiantao.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public BaseResponse<SafetyUser> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或者密码为空");
        }
        SafetyUser safetyUser = userService.userLogin(userLoginRequest, request);
        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号、密码或校验密码为空");
        }
        Long id = userService.userRegister(userRegisterRequest, request);
        return ResultUtils.success(id);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        Boolean b = userService.userLogout(request);
        return ResultUtils.success(b);
    }

    @PostMapping("/search")
    public BaseResponse<List<UserVO>> searchUsers(@RequestBody UserQueryRequest userQueryRequest,
                                                  HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<UserVO> userVOList= userService.searchUsers(userQueryRequest, request);
        return ResultUtils.success(userVOList);
    }

    @GetMapping("/get/login")
    public BaseResponse<SafetyUser> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        SafetyUser safetyUser = new SafetyUser();
        BeanUtils.copyProperties(loginUser, safetyUser);
        return ResultUtils.success(safetyUser);
    }

}
