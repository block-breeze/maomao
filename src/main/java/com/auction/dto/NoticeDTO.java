package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zaria
 * @Date 2021/3/27
 */
@Data
public class NoticeDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long meetId;
    private String title;
    private String meetName;

    //公告编号
    private String code;
    //公告状态
    private Integer state;
    private String content;
    private String shortDescribe;
    //驳回原因
    private String reject;
    private String publishBy;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    private String logo;
    //拍卖会状态 1即将开始 2正在进行 3已结束
    private Integer meetState;

    public NoticeDTO() {
    }

    public NoticeDTO(String title, Integer state) {
        this.title = title;
        this.state = state;
    }
}
