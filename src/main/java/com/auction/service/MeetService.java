package com.auction.service;

import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.MeetDTO;
import com.auction.entity.Meet;

import java.util.List;
import java.util.Map;

/**
 * @Author zaria
 * @Date 2021/3/28
 */
public interface MeetService{

    public Page<MeetDTO> meetList(MeetDTO search , PageSearch page)throws Exception;

    public void meetAdd(MeetDTO dto) throws  Exception;

    public void meetModify(MeetDTO dto) throws Exception;

    public void meetDelete(Long id) throws Exception;

    MeetDTO selectById (Long id) throws Exception;

    Meet getMeetInfoById (Long id) throws Exception;

    List<Map<String,Object>> noticesListTen(String noticeTitle);

}

