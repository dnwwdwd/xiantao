package com.hjj.xiantao.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostVO implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String tags;

    private String images;

    private Integer price;

    private Long thumbNum;

    private Long favourNum;

    private UserVO userVO;

}
