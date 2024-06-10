package com.hjj.xiantao.model.request.post;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PostAddRequest implements Serializable {

    private String title;

    private String content;

    private BigDecimal price;

    private List<String> tags;

    private List<String> images;

}