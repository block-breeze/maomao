package com.auction.mapper;

import com.auction.dto.LotDTO;

import com.auction.dto.WebAuctionDTO;
import com.auction.entity.Lot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LotMapper  extends BaseMapper<Lot> {
    //查询拍卖会表总数
    Long selectLotCount(Map<Object, Object> map);
    //查询拍卖会表
    List<LotDTO> selectLotList(Map<Object, Object> map);

    List<WebAuctionDTO>  getWebLotList(@Param("start") int start, @Param("count") int count,@Param("sort") Integer sort,@Param("meetId") Long meetId);

    int getWebLotCount(Map<Object, Object> map);


    List<WebAuctionDTO>  getWebAllLotListByMeetId(Long meetId);
}
