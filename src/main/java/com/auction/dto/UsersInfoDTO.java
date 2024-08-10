package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UsersInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String username;

    private String realName;

    private String phone;

    private String mail;

    private String idCard;

    private  Integer sex;
    //是否实名
    private Integer isReal;

    private String password;


    //管理员类型 只有管理员类型为2可以操作
    private Integer type;


    private String idBack;
    private String idFront;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public UsersInfoDTO(String username, String realName, Integer isReal) {
        this.username = username;
        this.realName = realName;
        this.isReal = isReal;
    }

    public UsersInfoDTO() {
    }
}
