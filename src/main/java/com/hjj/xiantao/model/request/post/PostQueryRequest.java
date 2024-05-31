package com.hjj.xiantao.model.request.post;

import com.hjj.xiantao.model.request.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostQueryRequest extends PageRequest implements Serializable {

    private String searchParam;

    private Integer minPrice;

    private Integer maxPrice;

    private List<String> tagList;

}
