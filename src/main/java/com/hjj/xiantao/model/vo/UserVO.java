package com.hjj.xiantao.model.vo;

import com.hjj.xiantao.model.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    private Long id;

    private String username;

    private String avatarUrl;

    private Integer gender;

    private String profile;

    private String phone;

    private String email;

    private String likedTags;

    public static UserVO userToUserVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
