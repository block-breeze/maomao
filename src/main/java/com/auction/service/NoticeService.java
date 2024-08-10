package com.auction.service;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;

import com.auction.dto.MeetDTO;
import com.auction.dto.NoticeDTO;

/**
 * @Author zaria
 * @Date 2021/3/27
 */
public interface NoticeService{
    //查询公告列表
    public Page<NoticeDTO> noticeList(NoticeDTO search, PageSearch page) throws Exception;
    //新增拍卖公告
    public void noticeAdd(NoticeDTO dto) throws Exception;
    //修改拍卖公告
    public void noticeModify(NoticeDTO dto) throws Exception;
    //根据id删除拍卖公告
    public void noticeDelete(Long id) throws Exception;
    //拍卖公告详情接口
    NoticeDTO getNoticeDetailById (Long id) throws Exception;
    //公告的发布
    public void publishById (Long id) throws Exception;

    //公告撤销
    public void reject (NoticeDTO dto) throws Exception;

    //关联发布会
    public JSONObject relationById (Long id) throws Exception;

    void cancelRelationById(Long id)throws Exception;


}
