package com.auction.mapper;

import com.auction.dto.BidderDTO;
import com.auction.entity.Bidder;
import com.auction.entity.UsersInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidderMapper extends BaseMapper<Bidder> {

    Bidder getByMeetIdAndBidderId(@Param("meetId") Long meetId, @Param("bidderId")  Long bidderId);
    //查询用户表总数
    Long selectBidderCount(Map<Object, Object> map);
    //查询用户表
    List<BidderDTO> selectBidderList(Map<Object, Object> map);


    //查询用户的所有报名信息
    /*List<BidderDTO> getBidderSignUpInfo(Long bidderId);*/
    UsersInfo getBidInfoByBidderId(Long bidderId);

}
