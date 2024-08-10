package com.auction.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("news")
public class News implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value="id",type= IdType.AUTO)
    private Long  id ;

    private String title;

    private Integer module;
    //发布状态 0未发布 1已发布 2已撤销
    private Integer state;

    private String  content;

    //驳回原因
    private String reject;

    //新闻浏览人数
    private Long readAmount;

    private String  publishBy;

    private Date publishTime;

    private Date updateTime;

    private Date createTime;
}
