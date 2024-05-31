package com.hjj.xiantao.model.request.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostAddRequest implements Serializable {

    private String title;

    private String content;

    private Integer price;

    private List<String> tags;

}