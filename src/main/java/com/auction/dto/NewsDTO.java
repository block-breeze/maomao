package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public NewsDTO() {

    }
    private Long  id ;

    private String title;

    private Integer module;

    private Integer state;

    private String  content;

    private String  publishBy;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Long readAmount;
    //驳回原因
    private String reject;

    public NewsDTO(String title, Integer module, Integer state, String publishBy) {
        this.title = title;
        this.module = module;
        this.state = state;
        this.publishBy = publishBy;
    }
}
