package com.hjj.xiantao.model.request.follow;

import lombok.Data;

@Data
public class FollowRequest {

    Long userId; // 被关注者 id

    Boolean isFollow; // 是否关注
}
