package com.auction.service;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.BidderDTO;
import com.auction.entity.Bidder;

public interface BidderService {

    public Page<BidderDTO> bidderList(BidderDTO dto, PageSearch page) throws Exception;

    public void add(BidderDTO dto) throws Exception;


    public Bidder bidderInfoById(Long bidderId, Long meetId) throws Exception;


    public JSONObject webGetMySignUpList(Long BidderId) throws Exception;

  //  public List<MeetDTO> webGetMySignUpList(Long BidderId) throws Exception;

}
