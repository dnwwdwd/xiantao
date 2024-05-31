package com.hjj.xiantao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.xiantao.model.domain.domain.Post;
import com.hjj.xiantao.service.PostService;
import com.hjj.xiantao.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author 何佳骏
* @description 针对表【post(帖子)】的数据库操作Service实现
* @createDate 2024-05-31 12:14:49
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {

}




