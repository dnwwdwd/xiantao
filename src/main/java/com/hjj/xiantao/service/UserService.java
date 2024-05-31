package com.hjj.xiantao.service;

import com.hjj.xiantao.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjj.xiantao.model.request.user.UserLoginRequest;
import com.hjj.xiantao.model.request.user.UserRegisterRequest;
import com.hjj.xiantao.model.vo.SafetyUser;

import javax.servlet.http.HttpServletRequest;

/**
* @author 何佳骏
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-30 22:52:30
*/
public interface UserService extends IService<User> {

    SafetyUser userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    Long userRegister(UserRegisterRequest userRegisterRequest, HttpServletRequest request);
}
