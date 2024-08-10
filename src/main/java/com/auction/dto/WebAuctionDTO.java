package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zaria
 * @Date 2021/4/13
 */
@Data
public class WebAuctionDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    //meet表
    private Long meetId;
    private String meetName;
    private String logo ;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer publishState;
    private Integer meetState;
    private String publishBy;
    private Integer lotCount;
    private String linkPhone;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date deadlineTime;

    //lot表
    private Long lotId;
    private String lotName;
    private String images;
    private BigDecimal startPrice;
    private BigDecimal dealPrice;
    private BigDecimal assessPrice;
    private BigDecimal increasePrice;
    private Integer auctionStatus;
    private String lotContent;


    //notice表
    private Long noticeId;
    private String noticeTitle;
    private String content;
    private String shortDescribe;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

}
