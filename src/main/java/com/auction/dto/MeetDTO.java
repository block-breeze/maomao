package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zaria
 * @Date 2021/3/28
 */
@Data
public class MeetDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long noticeId;
    private String name;
    private String noticeTitle;
    private String logo;
    //拍卖会开始时间
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String publishBy;
    private Integer meetState;
    private Integer publishState;
    private String linkMan;
    private String linkPhone;
    private Integer lotCount;

    //报名截止时间
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date deadlineTime;
    //拍卖会结束时间
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Long bidderId;


    public MeetDTO() {
    }

    public MeetDTO(String name, Integer meetState, Integer publishState) {
        this.name = name;
        this.meetState = meetState;
        this.publishState = publishState;
    }
}
