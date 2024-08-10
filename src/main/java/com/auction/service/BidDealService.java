package com.auction.service;

import com.alibaba.fastjson.JSONObject;
import com.auction.entity.BidDeal;

/**
 * @Author zaria
 * @Date 2021/5/5
 */
public interface BidDealService{

    public BidDeal dealInfoByLotId(Long lotId) throws Exception;

    public JSONObject webGetMyBidList(Long bidderId) throws Exception;

    public JSONObject webCheckPay(Long bidderId,Long lotId) throws Exception;

    public BidDeal webPay(Long bidderId, Long lotId) throws Exception;

}
