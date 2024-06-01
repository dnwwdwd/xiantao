package com.hjj.xiantao.service;

import com.hjj.xiantao.model.domain.PostFavour;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjj.xiantao.model.request.postfavour.PostFavourRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author 何佳骏
* @description 针对表【post_favour(帖子收藏)】的数据库操作Service
* @createDate 2024-05-31 12:14:49
*/
public interface PostFavourService extends IService<PostFavour> {

    Boolean addPostFavour(PostFavourRequest postFavourRequest, HttpServletRequest request);
    Boolean deletePostFavour(PostFavourRequest postFavourRequest, HttpServletRequest request);
}
