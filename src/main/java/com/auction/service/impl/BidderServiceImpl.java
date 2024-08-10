package com.auction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.BidUtil;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.common.ValidateUtil;
import com.auction.dto.BidderDTO;
import com.auction.dto.MeetDTO;
import com.auction.entity.Bidder;
import com.auction.entity.Meet;
import com.auction.entity.UsersInfo;
import com.auction.mapper.BidderMapper;
import com.auction.mapper.MeetMapper;
import com.auction.mapper.UsersInfoMapper;
import com.auction.service.BidderService;
import com.auction.service.SearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BidderServiceImpl extends ServiceImpl<BidderMapper, Bidder> implements BidderService {
    Logger logger = LoggerFactory.getLogger(BidderServiceImpl.class);

    @Autowired
    UsersInfoMapper usersInfoMapper;

    @Autowired
    MeetMapper meetMapper;

    @Autowired
    BidderMapper bidderMapper;

    @Autowired
    SearchService searchService;

    @Override
    public Page<BidderDTO> bidderList(BidderDTO dto, PageSearch page) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        //竞买人列表查询
        map.put("meetId",dto.getMeetId());

        int start = page.getStart()*page.getCount();

        map.put("sortorder", page.getSortorder());
        map.put("sortname", page.getSortname());
        map.put("start", start);
        map.put("count", page.getCount());
        Page<BidderDTO> result = new Page<BidderDTO>();
        int pageSize = page.getCount() < 1 ? 10 : page.getCount();
        int pageIndex = page.getStart() < 0 ? 0 : start / pageSize;
        Long totalCount = searchService.selectBidderCount(map);
        List<BidderDTO> list = searchService.selectBidderList(map);
        List<BidderDTO> dtoList = this.convertDTO(list);

        result.setCount(page.getCount());
        result.setPage(pageIndex + 1);
        result.setTotalCount(totalCount);
        result.setTotalPages(totalCount.intValue() / pageSize + 1);
        result.setItems(dtoList);
        return result;
    }
    private List<BidderDTO> convertDTO(List<BidderDTO> list) {
        for (BidderDTO user : list) {
            user.setPhone(ValidateUtil.validateMobile(user.getPhone()));
            user.setIdCard(ValidateUtil.validateIdCard(user.getIdCard()));
        }
        return list;
    }
    @Override
    public void add(BidderDTO dto) throws Exception {
        logger.info("竞买人管理——新增竞买人");
        if (null == dto) {
            throw new Exception("前台传递的数据异常");
        }
        if(null == dto.getMeetId()){
            throw new Exception("请选择拍卖会后新增");
        }
        //验证拍卖会相关
        Meet meet = this.usersInfoMapper.checkMeet(dto.getMeetId());
        if(null == meet){
            throw new Exception("该拍卖会不存在");
        }
   /*     if(meet.getPublishState().equals(0)){
            throw new Exception("拍卖会未发布，新增拍卖会竞买人失败");
        }*/
        if(meet.getPublishState().equals(2)){
            throw new Exception("拍卖会已结束，新增拍卖会竞买人失败");
        }

        //验证注册用户信息表相关
        UsersInfo checkBid = this.usersInfoMapper.checkBid(dto.getBidderName(),dto.getIdCard(),dto.getPhone());
        if(null == checkBid){
            throw new Exception("信息校验失败，请检查选项信息；如未注册，请先注册并实名认证后再报名！");
        }
        if(!checkBid.getIsReal().equals(1)){
            throw new Exception("添加失败，该竞买人未未实名认证，请去网站完成实名认证后再报名！");
        }

        //开始新增
        Bidder bidder = new Bidder();
        Bidder bidderMeet = bidderMapper.getByMeetIdAndBidderId(dto.getMeetId(),checkBid.getId());
        if(null != bidderMeet){
            throw new Exception("添加失败，该竞买人已报名本场拍卖会");
        }

        bidder.setBidderId(checkBid.getId());
        bidder.setMeetId(dto.getMeetId());
        bidder.setBidderNum(BidUtil.genRandomNum());
        bidder.setCreateTime(new Date());
        this.save(bidder);

        logger.info("id为："+dto.getMeetId()+"的拍卖会新增竞买人");

    }

    @Override
    public Bidder bidderInfoById(Long bidderId, Long meetId) throws Exception {
        return bidderMapper.getByMeetIdAndBidderId(meetId, bidderId);
    }

    @Override
    public JSONObject webGetMySignUpList(Long BidderId) throws Exception {
        JSONObject result = new JSONObject();
        List<MeetDTO> meetList = this.meetMapper.getMeetByBidderId(BidderId);
        if(null == meetList){
            result.put("msg","您未报名过拍卖会");
            return result;
        }
        List<MeetDTO> items = new ArrayList<MeetDTO>();
        for (MeetDTO meet: meetList) {
            MeetDTO dto = new MeetDTO();
            dto.setId(meet.getId());
            dto.setName(meet.getName());
            dto.setMeetState(meet.getMeetState());
            dto.setStartTime(meet.getStartTime());
            dto.setEndTime(meet.getEndTime());
            dto.setLinkMan(meet.getLinkMan());
            dto.setLinkPhone(meet.getLinkPhone());
            dto.setLogo(meet.getLogo());
            dto.setLotCount(meet.getLotCount());
            items.add(dto);
        }
        result.put("item", items);
        return result;
    }
}
