package com.auction.mapper;

import com.auction.dto.MeetDTO;

import com.auction.dto.NoticeDTO;
import com.auction.dto.WebAuctionDTO;
import com.auction.entity.Meet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author zaria
 * @Date 2021/3/28
 */
@Mapper
public interface MeetMapper extends BaseMapper<Meet>{
    //查询拍卖会表总数
    Long selectMeetCount(Map<Object, Object> map);
    //查询拍卖会表
    List<MeetDTO> selectMeetList(Map<Object, Object> map);

    MeetDTO getMeetById(Long id);

    NoticeDTO getNoticeNameByNoticeId(Long noticeId);

    List<Map<String, Object>> noticesListTen(Map<String, Object> paramMap);
    /**
     * 前台
     */
    List<WebAuctionDTO>  getWebMeetList(@Param("start") int start, @Param("count") int count, @Param("meetName") String meetName,@Param("meetState")  Integer meetState);
    int getWebMeetCount(Map<Object, Object> map);

    MeetDTO getNoticeTitleByMeetId(Long meetId);

    Meet getMeetByNoticeId(Long noticeId);

    Meet checkNameIsExist(@Param("id") Long id,@Param("name") String meetName);

    List<MeetDTO> getMeetByBidderId(Long bidderId);




}
