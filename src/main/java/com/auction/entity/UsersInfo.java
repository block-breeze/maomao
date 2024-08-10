package com.auction.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("users_info")
public class UsersInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    private String username;

    private String realName;

    private String phone;

    private String mail;

    private  Integer sex;

    private String password;

    //是否实名
    private Integer isReal;

    private String idCard;

    private String idBack;
    private String idFront;
    private Integer isAdminReset;
    private Date createTime;
    private Date updateTime;

}
