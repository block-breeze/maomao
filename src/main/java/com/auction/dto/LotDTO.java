package com.auction.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class LotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long meetId;
    private String meetName;
    private String content;
    private String images;
    private Integer auctionStatus;
    private Integer orderSort;
    private BigDecimal dealPrice;
    private BigDecimal startPrice;
    private BigDecimal assessPrice;
    private BigDecimal increasePrice;
    private String remarks;

    //meet表的字段
    private Integer meetState;



    public LotDTO(String name, String meetName, Long meetId,Integer auctionStatus) {
        this.name = name;
        this.meetName = meetName;
        this.auctionStatus = auctionStatus;
        this.meetId = meetId;
    }

    public LotDTO() {
    }
}
