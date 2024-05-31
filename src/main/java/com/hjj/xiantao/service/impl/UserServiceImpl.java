package com.hjj.xiantao.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.constant.UserConstant;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.mapper.UserMapper;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.request.user.UserLoginRequest;
import com.hjj.xiantao.model.request.user.UserQueryRequest;
import com.hjj.xiantao.model.request.user.UserRegisterRequest;
import com.hjj.xiantao.model.vo.SafetyUser;
import com.hjj.xiantao.model.vo.UserVO;
import com.hjj.xiantao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.hjj.xiantao.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 何佳骏
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-05-30 22:52:30
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    public static final String SALT = "hejiajun";
    @Override
    public SafetyUser userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 1.校验参数
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号应不少于 4 位");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码应不少于 6 位");
        }
        // 2.密码加密
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3.查询账户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptedPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不正确");
        }
        // 4.返回脱敏 User
        SafetyUser safetyUser = new SafetyUser();
        BeanUtils.copyProperties(user, safetyUser);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public synchronized Long userRegister(UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 1，校验参数
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号应不少于 4 位");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码应不少于 6 位");
        }
        if (!StrUtil.equals(userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码与校验密码不一致");
        }
        // 2，查询账号是否已被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已被注册");
        }
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + checkPassword).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        boolean save = this.save(user);
        if (!save) {
            log.info("用户注册信息保存至数据库失败");
        }
        return user.getId();
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        User loginUser = this.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object obj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User loginUser = (User) obj;
        if (loginUser == null || loginUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        loginUser = this.getById(loginUser.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return loginUser;
    }

    @Override
    public List<UserVO> searchUsers(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        List<User> userList = this.list(getQueryWrapper(userQueryRequest));
        List<UserVO> userVOList = userList.stream().map(UserVO::userToUserVO).collect(Collectors.toList());
        return userVOList;
    }


    private QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        String username = userQueryRequest.getUsername();
        String userAccount = userQueryRequest.getUserAccount();
        Integer gender = userQueryRequest.getGender();
        String profile = userQueryRequest.getProfile();
        String phone = userQueryRequest.getPhone();
        String email = userQueryRequest.getEmail();
        Integer userStatus = userQueryRequest.getUserStatus();
        String orderName = userQueryRequest.getOrderName();
        String asc = userQueryRequest.getAsc();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        queryWrapper.eq(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.eq(gender != null && gender >= 0, "gender", gender);
        queryWrapper.like(StrUtil.isNotBlank(profile), "profile", profile);
        queryWrapper.eq(StrUtil.isNotBlank(phone), "phone", phone);
        queryWrapper.eq(StrUtil.isNotBlank(email), "email", email);
        queryWrapper.eq(userStatus != null && userStatus >= 0, "userStatus", userStatus);
        queryWrapper.orderBy(StrUtil.isNotBlank(orderName), "sc".equals(asc), orderName);
        return queryWrapper;
    }

}




