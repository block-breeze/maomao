package com.auction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.MeetDTO;
import com.auction.dto.NoticeDTO;

import com.auction.entity.Meet;
import com.auction.entity.Notice;
import com.auction.mapper.MeetMapper;
import com.auction.mapper.NoticeMapper;
import com.auction.service.NoticeService;
import com.auction.service.SearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zaria
 * @Date 2021/3/27
 */
@Service
public class NoticeServiceImpl  extends ServiceImpl<NoticeMapper, Notice> implements NoticeService{
    Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    SearchService searchService;
    @Autowired
    MeetMapper meetMapper;

    @Override
    public Page<NoticeDTO> noticeList(NoticeDTO search, PageSearch page) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        map.put("title", search.getTitle());
        map.put("state", search.getState());
        int start = page.getStart()*page.getCount();
        map.put("sortorder", page.getSortorder());
        map.put("sortname", page.getSortname());
        map.put("start", start);
        map.put("count", page.getCount());
        Page<NoticeDTO> result = new Page<NoticeDTO>();
        int pageSize = page.getCount() < 1 ? 10 : page.getCount();
        int pageIndex = page.getStart() < 0 ? 0 : start / pageSize;
        Long totalCount = searchService.selectNoticeCount(map);
        List<NoticeDTO> list = searchService.selectNoticeList(map);
        result.setCount(page.getCount());
        result.setPage(pageIndex + 1);
        result.setTotalCount(totalCount);
        result.setTotalPages(totalCount.intValue() / pageSize + 1);
        result.setItems(list);
        return result;
    }
/*
    private List<NoticeDTO> convertDTO(List<NoticeDTO> list) {
        List<NoticeDTO> dtoList = new ArrayList<>();
        for (NoticeDTO noticeDTO : list) {
            // for (int i = 0; i < list.size(); i++) {

            NoticeDTO dto = new NoticeDTO();
            dto.setId(noticeDTO.getId());
            dto.setTitle(noticeDTO.getTitle());
            dto.setState(noticeDTO.getState());
            dto.setMeetId(noticeDTO.getMeetId());
            dto.setMeetName(noticeDTO.getMeetName());
            dto.setPublishTime(noticeDTO.getPublishTime());
            dto.setPublishBy(noticeDTO.getPublishBy());
            dto.setContent(noticeDTO.getContent());
            dto.setReject(noticeDTO.getReject());
            dto.setShortDescribe(noticeDTO.getShortDescribe());
            dtoList.add(dto);
        }
        return dtoList;
    }*/
    @Override
    public void noticeAdd(NoticeDTO dto) throws Exception {
        logger.info("拍卖公告管理——新增");
        if (null == dto) {
            throw new Exception("前台传递的数据异常");
        }
        //判断数据不为空
        if (null == dto.getTitle()  || null == dto.getShortDescribe() || null == dto.getContent() || null == dto.getPublishBy()) {
            throw new Exception("必填参数不得为空");
        }
        Notice check = this.noticeMapper.checkNoticeTitle(dto.getId(),dto.getTitle());
        if(null != check){
            throw new Exception("该公告已存在");
        }
        Notice notice = new Notice();

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setShortDescribe(dto.getShortDescribe());
        notice.setPublishBy(dto.getPublishBy());
        //生成不重复的code
        String noticeCode = UUID.randomUUID().toString().replace("-","").toUpperCase();
        notice.setNoticeCode(noticeCode);
        notice.setCreateTime(new Date());
        notice.setState(0);
        this.save(notice);
        logger.info("新增id为：" + notice.getId());
    }

    @Override
    public void noticeModify(NoticeDTO dto) throws Exception {
        logger.info("拍卖公告管理——修改");
        if (null == dto || null == dto.getId()) {
            throw new Exception("数据异常！");
        }

        Notice notice  = noticeMapper.selectById(dto.getId());
        if (null == notice) {
            throw new Exception("此信息不存在！");
        }
        if(notice.getState().equals(1)){
            throw new Exception("已发布的公告不可修改！请撤销发布");
        }
        //判断数据不为空
        if (null == dto.getTitle() || null == dto.getContent() || null == dto.getPublishBy()) {
            throw new Exception("必填参数不得为空");
        }
        Notice check = this.noticeMapper.checkNoticeTitle(dto.getId(),dto.getTitle());
        if(null != check){
            throw new Exception("该公告已存在");
        }
        notice.setTitle(dto.getTitle());
        notice.setShortDescribe(dto.getShortDescribe());
        notice.setContent(dto.getContent());
        notice.setPublishBy(dto.getPublishBy());
        notice.setUpdateTime(new Date());
        this.noticeMapper.updateById(notice);

        logger.info("修改id为：" + notice.getId());
    }

    @Override
    public void noticeDelete(Long id) throws Exception {
        logger.info("拍卖公告管理——删除");
        if (null == id) {
            throw new Exception("数据异常！");
        }
        Notice notice = noticeMapper.selectById(id);
        if (null == notice) {
            throw new Exception("此信息不存在！");
        }
        if(null != notice.getMeetId()&& null != notice.getMeetName()){
            throw new Exception("此拍卖公告已关联拍卖会不可删除！如果删除，请取消关联");
        }
        if(notice.getState().equals(1)){
            throw new Exception("已经发布的公告不可删除！");
        }
        this.noticeMapper.deleteById(id);

        logger.info("删除id为：" + notice.getId());

    }

    @Override
    public NoticeDTO getNoticeDetailById(Long id) throws Exception {
        logger.info("拍卖公告管理——查看详情，id："+id);
        NoticeDTO noticeDetail = noticeMapper.getNoticeDetailById(id);

        if (noticeDetail == null ) {
            throw new Exception("没有找到对应id的拍卖会");
        }
        if(!noticeDetail.getState().equals(1)){
            throw new Exception("该公告未发布不能查看！请从公告列表页重新进入！");
        }

        return noticeDetail;
    }

    @Override
    public void publishById(Long id) throws Exception {
        logger.info("拍卖公告管理——发布");
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new Exception("没有找到对应id的公告");
        }
        if(notice.getState() == 1){
            throw new Exception("该公告已发布!请不要重复发布");
        }
        if(null ==notice.getMeetId()||null == notice.getMeetName()){
            throw new Exception("该公告未关联拍卖会不可发布");
        }
        //公告发布，拍卖会也发布，拍卖会状态为即将开始
        Meet meet = meetMapper.getMeetByNoticeId(id);
        meet.setPublishState(1);
        meet.setMeetState(1);
        meet.setPublishTime(new Date());
        meet.setUpdateTime(new Date());
        this.meetMapper.updateById(meet);
        notice.setState(1);
        notice.setPublishTime(new Date());
        notice.setUpdateTime(new Date());
        this.updateById(notice);
        logger.info("拍卖公告管理——发布,发布id为："+id);
    }

    @Override
    public void reject(NoticeDTO dto) throws Exception {
        if(null == dto ||null == dto.getId()){
            throw new Exception("前台传递数据异常");
        }
        Notice notice = noticeMapper.selectById(dto.getId());
        if (notice == null) {
            throw new Exception("没有找到对应id的公告");
        }
        if(notice.getState() == 2){
            throw new Exception("该公告已撤销!");
        }
        Meet meet = meetMapper.getMeetByNoticeId(dto.getId());
        meet.setPublishState(2);
        meet.setPublishTime(null);
        meet.setUpdateTime(new Date());
        this.meetMapper.updateById(meet);
        notice.setState(2);
        notice.setPublishTime(null);
        notice.setUpdateTime(new Date());
        notice.setReject(dto.getReject());
        this.updateById(notice);
        logger.info("新闻管理——撤销，撤销id为："+dto.getId());
    }

    @Override
     public JSONObject relationById(Long id) throws Exception {
        logger.info("拍卖公告管理——关联拍卖会");

        Notice notice  = noticeMapper.selectById(id);
        if (null == notice) {
            throw new Exception("此公告信息不存在！");
        }
        if(notice.getState().equals(1)){
            throw new Exception("已发布的公告不可操作！");
        }
        if(null != notice.getMeetId()||null != notice.getMeetName()){
            throw new Exception("该公告已关联拍卖会！");
        }
        Meet meet = noticeMapper.relationById(id);
        if(null == meet){
            throw new Exception("该公告下没有拍卖会关联！");
        }
        notice.setMeetId(meet.getId());
        notice.setMeetName(meet.getName());
        this.updateById(notice);

        JSONObject jo = new JSONObject();
        jo.put("meetName",meet.getName());
        logger.info("id为："+id+"的拍卖公告，"+"关联的拍卖会id为：" + meet.getId());
        return jo;
    }

    @Override
    public void cancelRelationById(Long id) throws Exception {
        logger.info("拍卖公告管理——取消关联拍卖会");
        NoticeDTO checkNotice = noticeMapper.cancelRelationById(id);
        if (null == checkNotice) {
            throw new Exception("此公告并没有关联拍卖会");
        }
        Notice notice  = noticeMapper.selectById(id);
        if (null == notice) {
            throw new Exception("此公告信息不存在！");
        }
        if(notice.getState().equals(1)){
            throw new Exception("已发布的公告不可操作！");
        }
        notice.setMeetId(null);
        notice.setMeetName(null);
        this.updateById(notice);

    }

}
