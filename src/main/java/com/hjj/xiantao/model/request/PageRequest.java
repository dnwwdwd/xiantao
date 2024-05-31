package com.hjj.xiantao.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

    private long pageSize;

    private long pageNum;

    private String orderName;

    private String asc;
}
