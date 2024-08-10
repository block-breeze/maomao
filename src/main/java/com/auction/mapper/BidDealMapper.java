package com.auction.mapper;

import com.auction.dto.BidDealDTO;
import com.auction.entity.BidDeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zaria
 * @Date 2021/5/5
 */
@Mapper
public interface BidDealMapper extends BaseMapper<BidDeal>{

    BidDeal dealInfoByLotId(Long lotId) ;

    List<BidDealDTO> getWebMyBidList(Long bidderId);

    BidDealDTO getWebCheckPay(@Param("bidderId")  Long bidderId, @Param("lotId") Long lotId);

    BidDeal getWebPay(@Param("bidderId") Long bidderId,@Param("lotId") Long lotId);





}
