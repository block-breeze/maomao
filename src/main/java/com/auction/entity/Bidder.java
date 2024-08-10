package com.auction.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;
@Data
@TableName("bidder")
public class Bidder {
    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    private Long bidderId;
    //竞买号
    private String bidderNum;

    private Long meetId;

    private Date createTime;
}
