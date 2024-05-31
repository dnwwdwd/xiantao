package com.hjj.xiantao.model.request.user;

import com.hjj.xiantao.model.request.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    private String username;

    private String userAccount;

    private Integer gender;

    private String profile;

    private String phone;

    private String email;

    /**
     * 状态 0 - 正常 1-禁止 2-封号
     */
    private Integer userStatus;

    private List<String> likedTags;

}
