package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class BidderDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long bidderId;

    private String bidderName;

    private String idCard;

    private String phone;
    //竞买号
    private String bidderNum;

    private Long meetId ;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public BidderDTO() {
    }

    public BidderDTO(Long meetId) {
        this.meetId = meetId;
    }
}
