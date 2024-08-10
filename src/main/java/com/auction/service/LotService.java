package com.auction.service;

import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.LotDTO;
import com.auction.entity.Lot;


public interface LotService {
    public Page<LotDTO> lotList(LotDTO search , PageSearch page)throws Exception;

    //拍卖会新增标的
    public void lotAdd(LotDTO dto) throws  Exception;

    public void lotRemarks(LotDTO dto) throws  Exception;

    public void lotModify(LotDTO dto) throws Exception;

    public void lotDelete(Long id) throws Exception;

    public Lot lotInfoById(Long id) throws Exception;


}
