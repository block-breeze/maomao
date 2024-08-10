package com.auction.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zaria
 * @Date 2021/3/27
 */
@Data
@TableName("notice")
public class Notice implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId(value="id",type= IdType.AUTO)
    private Long id;
    private Long meetId;
    private String title;
    private String meetName;

    //公告编号
    private String noticeCode;

    //公告状态
    private Integer state;
    private String content;
    private String shortDescribe;
    //驳回原因
    private String reject;
    private String publishBy;
    private Date publishTime;
    private Date createTime;
    private Date updateTime;


}
