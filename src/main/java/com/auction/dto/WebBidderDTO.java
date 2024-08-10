package com.auction.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebBidderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long bidderId;

    private String bidderName;



    private Integer isReal;

    private String idCard;

    private String phone;
    //竞买号
    private String bidderNum;

    private Long meetId ;
    private String meetName ;


    private String noticeTitle ;
}
