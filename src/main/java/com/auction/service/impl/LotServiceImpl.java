package com.auction.service.impl;

import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.LotDTO;
import com.auction.entity.Lot;
import com.auction.entity.Meet;
import com.auction.mapper.LotMapper;
import com.auction.mapper.MeetMapper;
import com.auction.service.LotService;
import com.auction.service.SearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LotServiceImpl  extends ServiceImpl<LotMapper, Lot> implements LotService {
    Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);
    @Autowired
    MeetMapper meetMapper;
    @Autowired
    SearchService searchService;
    @Autowired
    LotMapper lotMapper;

    @Override
    public Page<LotDTO> lotList(LotDTO search, PageSearch page) throws Exception {
        Map<Object,Object> map =new HashMap<>();
        map.put("name",search.getName());
        map.put("meetName",search.getMeetName());
        map.put("auctionStatus",search.getAuctionStatus());
        map.put("meetId",search.getMeetId());


        int start = page.getStart()*page.getCount();
        map.put("sortorder", page.getSortorder());
        map.put("sortname", page.getSortname());
        map.put("start", start);
        map.put("count", page.getCount());

        Page<LotDTO> result = new Page<LotDTO>();
        int pageSize = page.getCount() < 1 ? 10 : page.getCount();
        int pageIndex = page.getStart() < 0 ? 0 : start / pageSize;
        Long totalCount = searchService.selectLotCount(map);
        List<LotDTO> list = searchService.selectLotList(map);
        /*List<LotDTO> dtoList = this.convertDTO(list);*/

        result.setCount(page.getCount());
        result.setPage(pageIndex + 1);
        result.setTotalCount(totalCount);
        result.setTotalPages(totalCount.intValue() / pageSize + 1);
        result.setItems(list);
        return result;
    }
/*

    private List<LotDTO> convertDTO(List<LotDTO> list) {
        List<LotDTO> dtoList = new ArrayList<>();
        for (LotDTO lot : list) {
            LotDTO dto = new LotDTO();
            dto.setId(lot.getId());
            dto.setName(lot.getName());
            dto.setStartPrice(lot.getStartPrice());
            dto.setMeetName(lot.getMeetName());
            dto.setMeetId(lot.getMeetId());
            dto.setAuctionStatus(lot.getAuctionStatus());
            dto.setAssessPrice(lot.getAssessPrice());
            dto.setDealPrice(lot.getDealPrice());
            dto.setRemarks(lot.getRemarks());
            dto.setOrderSort(lot.getOrderSort());
            dto.setImages(lot.getImages());
            dtoList.add(dto);
        }
        return dtoList;
    }
*/

    @Override
    public void lotAdd(LotDTO dto) throws Exception {
        logger.info("拍卖会新增标的");
        Meet meet = meetMapper.selectById(dto.getMeetId());
        if(null == meet){
            throw new Exception("此拍卖会信息不存在！");
        }
      /*  if(meet.getPublishState().equals(1)){
            throw new Exception("已发布的拍卖会不可操作！");
        }*/
        if(meet.getStartTime().equals(new Date())){
            throw new Exception("拍卖会已经开始，不能再进行拍品新增！");
        }
        if(null == dto.getName()||null==dto.getStartPrice()|| null == dto.getIncreasePrice()|| null == dto.getContent()){
            throw new Exception("必填参数不得为空");
        }
        Lot lot =new Lot();
        lot.setName(dto.getName());
        lot.setMeetId(dto.getMeetId());
        lot.setStartPrice(dto.getStartPrice());
        lot.setAssessPrice(dto.getAssessPrice());
        lot.setIncreasePrice(dto.getIncreasePrice());
        lot.setImages(dto.getImages());
        lot.setContent(dto.getContent());
        lot.setOrderSort(dto.getOrderSort());
        lot.setAuctionStatus(0);
        lot.setCreateTime(new Date());
        this.save(lot);
    }
    @Override
    public void lotModify(LotDTO dto) throws Exception {
        logger.info("标的管理——修改");
        if (null == dto || null == dto.getId()) {
            throw new Exception("数据异常！");
        }

        if(null == this.getById(dto.getId())){
            throw new Exception("此信息不存在！");
        }
        if(null == dto.getName()||null==dto.getStartPrice()|| null == dto.getIncreasePrice()|| null == dto.getContent()){
            throw new Exception("必填参数不得为空");
        }
        Lot lot =lotMapper.selectById(dto.getId());
        lot.setName(dto.getName());
        lot.setStartPrice(dto.getStartPrice());
        lot.setAssessPrice(dto.getAssessPrice());
        lot.setIncreasePrice(dto.getIncreasePrice());
        lot.setImages(dto.getImages());
        lot.setContent(dto.getContent());
        lot.setOrderSort(dto.getOrderSort());
        lot.setUpdateTime(new Date());
        this.lotMapper.updateById(lot);
        logger.info("修改id为：" + dto.getId());

    }

    @Override
    public void lotDelete(Long id) throws Exception {
        logger.info("标的管理——删除");
        if (null == id) {
            throw new Exception("数据异常！");
        }
        Lot lot = lotMapper.selectById(id);
        if(null == lot){
            throw new Exception("此信息不存在！");
        }
        Meet meet = meetMapper.selectById(lot.getMeetId());
        if(meet.getMeetState().equals(2)){
            throw new Exception("拍卖会正在进行，标的不可删除！");
        }
        this.lotMapper.deleteById(id);
        logger.info("删除id为：" + lot.getId());
    }

    @Override
    public Lot lotInfoById(Long id) throws Exception {

        if (null == id) {
            throw new Exception("数据异常！");
        }
        Lot lot = lotMapper.selectById(id);
        if(null == lot){
            throw new Exception("此信息不存在！");
        }
        return lot;
    }

    @Override
    public void lotRemarks(LotDTO dto) throws Exception {
        Lot lot = this.lotMapper.selectById(dto.getId());
        if(null == lot){
            throw new Exception("此标的信息不存在！");
        }
        lot.setRemarks(dto.getRemarks());
        lot.setUpdateTime(new Date());
        this.updateById(lot);


    }


}
