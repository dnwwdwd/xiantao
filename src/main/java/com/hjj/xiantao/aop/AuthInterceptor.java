package com.hjj.xiantao.aop;

import cn.hutool.core.util.StrUtil;
import com.hjj.xiantao.annotation.AuthCheck;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.enums.UserRoleEnum;
import com.hjj.xiantao.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//@Component
//@Aspect
//public class AuthInterceptor {
//
//    @Resource
//    private UserService userService;
//
//    @Around("@annotation(authCheck)")
//    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
//        String mustRole = authCheck.mustRole();
//        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
//        User loginUser = userService.getLoginUser(request);
//        // 必须有权下才能通过
//        if (StrUtil.isNotBlank(mustRole)) {
//            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
//            if (mustUserRoleEnum == null) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR.NO_AUTH);
//            }
//            String userRole = loginUser.getUserRole();
//            if (!UserRoleEnum.ADMIN.equals(mustUserRoleEnum) || !mustRole.equals(userRole)) {
//                throw new BusinessException(ErrorCode.NO_AUTH);
//            }
//
//        }
//        // 通过校验放行
//        return joinPoint.proceed();
//    }
//}
