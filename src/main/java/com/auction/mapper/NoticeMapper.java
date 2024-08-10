package com.auction.mapper;


import com.auction.dto.MeetDTO;
import com.auction.dto.NoticeDTO;
import com.auction.dto.WebAuctionDTO;
import com.auction.entity.Meet;
import com.auction.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author zaria
 * @Date 2021/3/27
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice>{
    //查询公告表总数
    Long selectNoticeCount(Map<Object, Object> map);
    //查询公告表
    List<NoticeDTO> selectNoticeList(Map<Object, Object> map);

    NoticeDTO getNoticeDetailById(Long id);

    Meet relationById (Long id);

    NoticeDTO cancelRelationById(Long id);

    Notice checkNoticeTitle(@Param("id") Long noticeId, @Param("title")String noticeTitle);
    /**
     * 前台
     */
    WebAuctionDTO getWebNoticeById(Long meetId);

    List<WebAuctionDTO> getWebNoticeList(@Param("start") int start, @Param("count") int count, @Param("noticeTitle") String noticeTitle);

    int getWebNoticeCount(Map<Object, Object> map);
}
