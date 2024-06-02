package com.hjj.xiantao.service;

import com.hjj.xiantao.model.domain.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjj.xiantao.model.request.DeleteRequest;
import com.hjj.xiantao.model.request.post.PostAddRequest;
import com.hjj.xiantao.model.request.post.PostQueryRequest;
import com.hjj.xiantao.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 何佳骏
* @description 针对表【post(帖子)】的数据库操作Service
* @createDate 2024-05-31 12:14:49
*/
public interface PostService extends IService<Post> {

    Long addPost(PostAddRequest postAddRequest, HttpServletRequest request);

    Boolean deletePost(DeleteRequest deleteRequest, HttpServletRequest request);

    Boolean deletePostByMySelf(DeleteRequest deleteRequest, HttpServletRequest request);

    List<PostVO> recommendPosts(long pageNum, long pageSize, HttpServletRequest request);

    List<PostVO> searchPost(PostQueryRequest postQueryRequest, HttpServletRequest request);

    List<PostVO> listMyPost(long pageNum, long pageSize, HttpServletRequest request);
}
