package com.hjj.xiantao.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PostVO implements Serializable {

    private Long id;

    private String title;

    private String content;

    private List<String> tags;

    private List<String> images;

    private BigDecimal price;

    private Long thumbNum;

    private Long favourNum;

    private UserVO userVO;

}
