package com.auction.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zaria
 * @Date 2021/5/5
 */
@Data
public class BidDeal  implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long lotId;

    private String lotName;

    private Long bidderId;

    private String bidderNum;

    private Long meetId;

    private BigDecimal dealPrice;

    private Integer isPay;

    private Date createTime;

}
