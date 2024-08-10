package com.auction.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("lot")
public class Lot implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long meetId;
    private String content;
    private String images;
    private Integer auctionStatus;
    private Integer orderSort;
    private BigDecimal dealPrice;
    private BigDecimal startPrice;
    private BigDecimal assessPrice;
    private BigDecimal increasePrice;
    private Date createTime;
    private Date updateTime;
    private String remarks;



  /*   id bigint(20) PK
meet_id bigint(20)
start_price decimal(19,2)
assess_price decimal(19,2)
increase_price decimal(19,2)
content varchar(1000)
auction_atatus int(11)
create_time datetime
update_time datetime
images*/
}
