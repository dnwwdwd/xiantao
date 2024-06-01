package com.hjj.xiantao.model.request.postfavour;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子收藏请求
 */
@Data
public class PostFavourRequest implements Serializable {

    private Long postId; // 帖子id

    private Boolean isFavour; // 是否收藏
}
