package com.hjj.xiantao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.xiantao.common.ErrorCode;
import com.hjj.xiantao.exception.BusinessException;
import com.hjj.xiantao.model.domain.Follow;
import com.hjj.xiantao.model.domain.User;
import com.hjj.xiantao.model.request.DeleteRequest;
import com.hjj.xiantao.model.request.follow.FollowRequest;
import com.hjj.xiantao.service.FollowService;
import com.hjj.xiantao.mapper.FollowMapper;
import com.hjj.xiantao.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 何佳骏
* @description 针对表【follow(关注表)】的数据库操作Service实现
* @createDate 2024-07-02 17:33:48
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{

    @Resource
    private UserService userService;

    @Resource
    private FollowMapper followMapper;

    @Override
    public Boolean addFollow(FollowRequest followRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = followRequest.getUserId();
        Boolean isFollow = followRequest.getIsFollow();
        // 1.校验参数
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.判断是否关注
        if (isFollow != null && isFollow) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您已关注");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("followerId", loginUser.getId());
        Long count = followMapper.selectCount(queryWrapper);
        if (count != null && count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您已关注");
        }
        // 3.插入关系到 follow 表
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowerId(loginUser.getId());
        boolean save = this.save(follow);
        return save;
    }

    @Override
    public Boolean deleteFollow(FollowRequest followRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long userId = followRequest.getUserId();
        Boolean isFollow = followRequest.getIsFollow();
        // 1.校验参数
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.判断是否关注
        if (isFollow !=null && !isFollow) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您还未关注");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("followerId", loginUser.getId());
        Long count = followMapper.selectCount(queryWrapper);
        if (count != null && count < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您还未关注");
        }
        // 3.删除关系
        boolean remove = this.remove(queryWrapper);
        return remove;
    }
}




