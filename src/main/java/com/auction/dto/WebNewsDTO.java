package com.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class WebNewsDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long  id ;

    private String title;

    private String  publishBy;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
}
