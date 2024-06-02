package com.hjj.xiantao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.request.user.UserLoginRequest;
import com.hjj.xiantao.model.request.user.UserQueryRequest;
import com.hjj.xiantao.model.request.user.UserRegisterRequest;
import com.hjj.xiantao.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 何佳骏
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-30 22:52:30
*/
public interface UserService extends IService<User> {

    User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    Long userRegister(UserRegisterRequest userRegisterRequest, HttpServletRequest request);

    Boolean userLogout(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    List<UserVO> searchUsers(UserQueryRequest userQueryRequest, HttpServletRequest request);
}
