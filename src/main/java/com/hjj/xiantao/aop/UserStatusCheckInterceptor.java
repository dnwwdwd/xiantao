package com.hjj.xiantao.aop;

import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.enums.UserStatusEnum;
import com.hjj.xiantao.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//@Component
//@Aspect
//public class UserStatusCheckInterceptor {
//
//    @Resource
//    private UserService userService;
//
//    @Before("execution(* com.hjj.xiantao.controller.*.*(..)) && " +
//            "!execution(* com.hjj.xiantao.controller.UserController.userLogin(..)) && " +
//            "!execution(* com.hjj.xiantao.controller.UserController.userRegister(..))")
//    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable{
//        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        User loginUser = userService.getLoginUser(request);
//        Integer userStatus = loginUser.getUserStatus();
//        UserStatusEnum enumByValue = UserStatusEnum.getEnumByValue(userStatus);
//        if (!UserStatusEnum.NORMAL.equals(enumByValue)) {
//            throw new BusinessException(ErrorCode.NO_AUTH);
//        }
//        return joinPoint.proceed();
//    }
//}
