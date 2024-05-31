package com.hjj.xiantao.model.request.user;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String userAccount;

    private String userPassword;
}
