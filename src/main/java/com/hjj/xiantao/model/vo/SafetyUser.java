package com.hjj.xiantao.model.vo;

import lombok.Data;

@Data
public class SafetyUser {

    private Long id;

    private String username;

    private String userAccount;

    private String avatarUrl;

    private Integer gender;

    private String profile;

    private String phone;

    private String email;

    private String likedTags;
}
