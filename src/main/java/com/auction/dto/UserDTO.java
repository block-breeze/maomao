package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;
    /**
     * 验证密码
     */
    private String checkPassword;

    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 描述
     */
    private String perDescribe;

    public UserDTO(String username, String realName) {
        this.username = username;
        this.realName = realName;
    }
    public UserDTO() {
    }
}
