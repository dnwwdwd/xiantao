package com.hjj.xiantao.service;

import com.hjj.xiantao.model.domain.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjj.xiantao.model.request.posthumb.PostThumbRequest;
import com.hjj.xiantao.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 何佳骏
* @description 针对表【post_thumb(题目提交表)】的数据库操作Service
* @createDate 2024-05-31 12:14:49
*/
public interface PostThumbService extends IService<PostThumb> {

    Boolean addPostThumb(PostThumbRequest postThumbRequest, HttpServletRequest request);

    Boolean deletePostThumb(PostThumbRequest postThumbRequest, HttpServletRequest request);

    List<PostVO> listMyPostThumb(long pageNum, long pageSize, HttpServletRequest request);

}
