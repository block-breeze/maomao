package com.auction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auction.dto.BidDealDTO;
import com.auction.entity.BidDeal;
import com.auction.entity.UsersInfo;
import com.auction.mapper.BidDealMapper;
import com.auction.mapper.UsersInfoMapper;
import com.auction.service.BidDealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zaria
 * @Date 2021/5/5
 */
@Service
public class BidDealServiceImpl extends ServiceImpl<BidDealMapper, BidDeal> implements BidDealService{
    Logger logger = LoggerFactory.getLogger(BidDealServiceImpl.class);

    @Autowired
    BidDealMapper bidDealMapper;

    @Autowired
    UsersInfoMapper usersInfoMapper;
    @Override
    public BidDeal dealInfoByLotId(Long lotId) throws Exception {
        return this.bidDealMapper.dealInfoByLotId(lotId);
    }

    @Override
    public JSONObject webGetMyBidList(Long bidderId) throws Exception {
        JSONObject result = new JSONObject();

        List<BidDealDTO> dealList = this.bidDealMapper.getWebMyBidList(bidderId);
        if(null == dealList){
            result.put("msg","您未拍下任何标的");
            return result;
        }
        List<BidDealDTO> items = new ArrayList<BidDealDTO>();
        for (BidDealDTO bidDealDTO: dealList) {
            BidDealDTO dto = new BidDealDTO();
            dto.setId(bidDealDTO.getId());
            dto.setLotId(bidDealDTO.getLotId());
            dto.setLotName(bidDealDTO.getLotName());
            dto.setBidderId(bidDealDTO.getBidderId());
            dto.setBidderNum(bidDealDTO.getBidderNum());
            dto.setMeetId(bidDealDTO.getMeetId());
            dto.setDealPrice(bidDealDTO.getDealPrice());
            dto.setIsPay(bidDealDTO.getIsPay());
            dto.setCreateTime(bidDealDTO.getCreateTime());

            dto.setImages(bidDealDTO.getImages());
            dto.setLinkMan(bidDealDTO.getLinkMan());
            dto.setLinkPhone(bidDealDTO.getLinkPhone());
            items.add(dto);
        }
        result.put("item", items);
        return result;
    }



    @Override
    public JSONObject webCheckPay(Long bidderId, Long lotId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        UsersInfo info = this.usersInfoMapper.selectById(bidderId);
        jsonObject.put("realName",info.getRealName());
        jsonObject.put("idCard",info.getIdCard());
        jsonObject.put("phone",info.getPhone());
        BidDealDTO  dealInfo = this.bidDealMapper.getWebCheckPay(bidderId,lotId);
        jsonObject.put("lotName",dealInfo.getLotName());
        jsonObject.put("dealPrice",dealInfo.getDealPrice());


        return jsonObject;
    }

    @Override
    public BidDeal webPay(Long bidderId, Long lotId) throws Exception {

        BidDeal dealInfo = this.bidDealMapper.getWebPay(bidderId,lotId);
        dealInfo.setIsPay(1);
        this.updateById(dealInfo);
        return dealInfo;
    }

}
