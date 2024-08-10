package com.auction.service.impl;

import com.auction.common.Page;
import com.auction.common.PageSearch;

import com.auction.dto.MeetDTO;


import com.auction.dto.NoticeDTO;
import com.auction.entity.Meet;
import com.auction.mapper.MeetMapper;

import com.auction.service.MeetService;
import com.auction.service.SearchService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zaria
 * @Date 2021/3/28
 */
@Service
public class MeetServiceImpl extends ServiceImpl<MeetMapper, Meet> implements MeetService{
    Logger logger = LoggerFactory.getLogger(MeetServiceImpl.class);

    @Autowired
    MeetMapper meetMapper;
    @Autowired
    SearchService searchService;

    @Override
    public Page<MeetDTO> meetList(MeetDTO search, PageSearch page) throws Exception {
        Map<Object,Object> map =new HashMap<>();
        map.put("name",search.getName());
        map.put("publishState",search.getPublishState());
        map.put("meetState",search.getMeetState());

        int start = page.getStart()*page.getCount();
        map.put("sortorder", page.getSortorder());
        map.put("sortname", page.getSortname());
        map.put("start", start);
        map.put("count", page.getCount());

        Page<MeetDTO> result = new Page<MeetDTO>();
        int pageSize = page.getCount() < 1 ? 10 : page.getCount();
        int pageIndex = page.getStart() < 0 ? 0 : start / pageSize;
        Long totalCount = searchService.selectMeetCount(map);
        List<MeetDTO> list = searchService.selectMeetList(map);
        //List<MeetDTO> dtoList = this.convertDTO(list);

        result.setCount(page.getCount());
        result.setPage(pageIndex + 1);
        result.setTotalCount(totalCount);
        result.setTotalPages(totalCount.intValue() / pageSize + 1);
        result.setItems(list);
        return result;

    }
//
//    private List<MeetDTO> convertDTO(List<MeetDTO> list) {
//        List<MeetDTO> dtoList = new ArrayList<>();
//        for (MeetDTO MeetDTO : list) {
//            MeetDTO dto = new MeetDTO();
//            dto.setId(MeetDTO.getId());
//            dto.setName(MeetDTO.getName());
//            dto.setPublishState(MeetDTO.getPublishState());
//            dto.setMeetState(MeetDTO.getMeetState());
//            dto.setStartTime(MeetDTO.getStartTime());
//            dto.setPublishTime(MeetDTO.getPublishTime());
//            dto.setNoticeId(MeetDTO.getNoticeId());
//            dto.setNoticeTitle(MeetDTO.getNoticeTitle());
//            dto.setLotCount(MeetDTO.getLotCount());
//
//            dto.setEndTime(MeetDTO.getEndTime());
//            dto.setDeadlineTime(MeetDTO.getDeadlineTime());
//            dto.setLinkMan(MeetDTO.getLinkMan());
//            dto.setLinkPhone(MeetDTO.getLinkPhone());
//            dto.setLogo(MeetDTO.getLogo());
//
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }
    @Override
    public void meetAdd(MeetDTO dto) throws Exception {
        logger.info("拍卖会管理——新增");
        if (null == dto) {
            throw new Exception("前台传递的数据异常");
        }
        if(null == dto.getName() || null == dto.getStartTime()|| null== dto.getLogo()){
            throw new Exception("必填参数不得为空");
        }
        Meet meetCheck = this.meetMapper.getMeetByNoticeId(dto.getNoticeId());
        Meet checkNameIsExist = meetMapper.checkNameIsExist(dto.getId(),dto.getName());
        if(null != checkNameIsExist){
            throw new Exception("该拍卖会已存在，请不要重复添加");
        }
        if(null != meetCheck){
            throw new Exception("该公告已存在");
        }
        NoticeDTO getNoticeName = this.meetMapper.getNoticeNameByNoticeId(dto.getNoticeId());
        if(null == getNoticeName){
            throw new Exception("该公告id不正确");
        }
        Meet meet = new Meet();
        meet.setName(dto.getName());
        meet.setNoticeId(dto.getNoticeId());
        meet.setNoticeTitle(getNoticeName.getTitle());
        meet.setPublishBy(getNoticeName.getPublishBy());
        meet.setLinkMan(dto.getLinkMan());
        meet.setLinkPhone(dto.getLinkPhone());
        meet.setLogo(dto.getLogo());
        meet.setStartTime(dto.getStartTime());
        meet.setEndTime(dto.getEndTime());
        meet.setDeadlineTime(dto.getDeadlineTime());
        meet.setCreateTime(new Date());
        meet.setPublishState(0);
        //meet.setMeetState(1);
        this.save(meet);
        logger.info("新增id为：" + meet.getId());
    }

    @Override
    public void meetModify(MeetDTO dto) throws Exception {
        logger.info("拍卖会管理——修改");
        if (null == dto) {
            throw new Exception("前台传递的数据异常");
        }
        Meet meet = meetMapper.selectById(dto.getId());
        Meet checkNameIsExist = meetMapper.checkNameIsExist(dto.getId(),dto.getName());
        if(null != checkNameIsExist){
            throw new Exception("该拍卖会已存在，请不要重复添加");
        }
        if(null == dto.getName() || null == dto.getStartTime()|| null== dto.getLogo()){
            throw new Exception("必填参数不得为空");
        }
        if(meet.getPublishState().equals(1)){
            throw new Exception("已发布的拍卖会不可修改！请撤销发布");
        }
  /*      Meet meetCheck = this.meetMapper.getMeetByNoticeId(dto.getNoticeId());
        if(null != meetCheck){
            throw new Exception("该公告已存在");
        }*/
        NoticeDTO getNoticeName = this.meetMapper.getNoticeNameByNoticeId(dto.getNoticeId());
        if(null == getNoticeName){
            throw new Exception("该公告不存在");
        }
        meet.setName(dto.getName());
        meet.setNoticeId(dto.getNoticeId());
        meet.setNoticeTitle(getNoticeName.getTitle());
        meet.setPublishBy(getNoticeName.getPublishBy());
        meet.setLinkMan(dto.getLinkMan());
        meet.setLinkPhone(dto.getLinkPhone());
        meet.setEndTime(dto.getEndTime());
        meet.setDeadlineTime(dto.getDeadlineTime());
        meet.setLogo(dto.getLogo());
        meet.setStartTime(dto.getStartTime());
        meet.setUpdateTime(new Date());
      //  meet.setMeetState(0);
        this.meetMapper.updateById(meet);
        logger.info("修改id为：" + meet.getId());
    }

    @Override
    public void meetDelete(Long id) throws Exception {
        logger.info("拍卖会管理——删除");
        if (null == id) {
            throw new Exception("数据异常！");
        }
        Meet meet = meetMapper.selectById(id);
        if(null == meet){
            throw new Exception("此信息不存在！");
        }
        if(meet.getPublishState().equals(1)){
            throw new Exception("已经发布的拍卖会不可删除！");
        }
        this.meetMapper.deleteById(id);
        logger.info("删除id为：" + meet.getId());
    }

    @Override
    public MeetDTO selectById(Long id) throws Exception {
        logger.info("拍卖会管理——查看详情，id："+id);
        MeetDTO detail = meetMapper.getMeetById(id);
        if(null == detail){
            throw new Exception("没有找到对应id的拍卖会");
        }
        return detail;
    }

    @Override
    public Meet getMeetInfoById(Long id) throws Exception {
        return this.meetMapper.selectById(id);
    }

    @Override
    public List<Map<String, Object>> noticesListTen(String noticeTitle) {
        Map<String ,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("noticeTitle", noticeTitle==null||noticeTitle.length()<1?null:"%"+noticeTitle+"%");
        return meetMapper.noticesListTen(paramMap);
    }


}
