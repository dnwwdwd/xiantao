package com.hjj.xiantao.service;

import com.hjj.xiantao.model.domain.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjj.xiantao.model.request.DeleteRequest;
import com.hjj.xiantao.model.request.follow.FollowRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author 何佳骏
* @description 针对表【follow(关注表)】的数据库操作Service
* @createDate 2024-07-02 17:33:48
*/
public interface FollowService extends IService<Follow> {

    Boolean addFollow(FollowRequest followRequest, HttpServletRequest request);

    Boolean deleteFollow(FollowRequest followRequest, HttpServletRequest request);
}
