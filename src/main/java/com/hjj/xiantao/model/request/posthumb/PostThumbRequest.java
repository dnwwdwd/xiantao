package com.hjj.xiantao.model.request.posthumb;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子点赞请求
 */
@Data
public class PostThumbRequest implements Serializable {

    private Long postId; // 帖子 id

    private Boolean isThumb; // 用户是否点赞

}
