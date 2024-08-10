package com.auction.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class WebUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 用户名
     */
    private String username;
    /**
     * 类型
     */
    private Integer type;
}
