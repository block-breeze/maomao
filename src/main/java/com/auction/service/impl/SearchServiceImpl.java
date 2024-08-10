package com.auction.service.impl;

import com.auction.common.BidUtil;
import com.auction.common.Page;
import com.auction.common.ValidateUtil;
import com.auction.dto.*;
import com.auction.entity.Bidder;
import com.auction.entity.Meet;
import com.auction.entity.User;
import com.auction.entity.UsersInfo;
import com.auction.mapper.*;
import com.auction.service.LoginService;
import com.auction.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    MeetMapper meetMapper;
    @Autowired
    LotMapper lotMapper;
    @Autowired
    UsersInfoMapper usersInfoMapper;
    @Autowired
    BidderMapper bidderMapper;
    @Autowired
    LoginService loginService;

    /**
     * 用户管理
     */
    @Override
    public Long selectUserCount(Map<Object, Object> map) {
        return userMapper.selectUserCount(map);
    }

    @Override
    public List<UserDTO> selectUserList(Map<Object, Object> map) {
        return userMapper.selectUserList(map);
    }

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }


    /**
     * 新闻管理
     */
    @Override
    public Long selectNewsCount(Map<Object, Object> map) {
        return newsMapper.selectNewsCount(map);
    }

    @Override
    public List<NewsDTO> selectNewsList(Map<Object, Object> map) {
        return newsMapper.selectNewsList(map);
    }

    /**
     * 拍卖公告管理
     * @param map
     * @return
     */
    @Override
    public Long selectNoticeCount(Map<Object, Object> map) {
        return noticeMapper.selectNoticeCount(map);
    }

    @Override
    public List<NoticeDTO> selectNoticeList(Map<Object, Object> map) {
        return noticeMapper.selectNoticeList(map);
    }

    /**
     * 拍卖会管理
     * @param map
     * @return
     */
    @Override
    public Long selectMeetCount(Map<Object, Object> map) {
        return meetMapper.selectMeetCount(map);
    }

    @Override
    public List<MeetDTO> selectMeetList(Map<Object, Object> map) {
        return meetMapper.selectMeetList(map);
    }


    /**
     * 标的管理
     */
    @Override
    public Long selectLotCount(Map<Object, Object> map) {
        return lotMapper.selectLotCount(map);
    }

    @Override
    public List<LotDTO> selectLotList(Map<Object, Object> map) {
        return lotMapper.selectLotList(map);
    }

    /**
     * 注册用户信息管理
     * @param map
     * @return
     */
    @Override
    public Long selectUsersInfoCount(Map<Object, Object> map) {
        return usersInfoMapper.selectUsersInfoCount(map);
    }

    @Override
    public List<UsersInfoDTO> selectUsersInfoList(Map<Object, Object> map) {
        return usersInfoMapper.selectUsersInfoList(map);
    }

    /**
     * 竞买人管理
     * @param map
     * @return
     */
    @Override
    public Long selectBidderCount(Map<Object, Object> map) {
        return bidderMapper.selectBidderCount(map);
    }

    @Override
    public List<BidderDTO> selectBidderList(Map<Object, Object> map) {
        return bidderMapper.selectBidderList(map);
    }

    /**
     * 前端页面
     * @param count
     * @return
     * @throws Exception
     */
    @Override
    public List<WebNewsDTO> getWebNews(int count) throws Exception {
        List<WebNewsDTO> list = new ArrayList<WebNewsDTO>();

        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("count", count);
            list = newsMapper.getWebNews(paramMap);
        } catch (Exception e) {
            throw new Exception("首页查询新闻出错");
        }
        return list;
    }

    @Override
    public Page<WebNewsDTO> getWebNewsList(int start, int count) throws Exception {
        try {
            List<WebNewsDTO> list = newsMapper.getWebNewsList(start * count, count);
            int totalCount = newsMapper.getWebNewsCount();
            Page<WebNewsDTO> page = new Page<WebNewsDTO>();
            page.setItems(list);
            page.setPage(start + 1);
            page.setCount(count);
            page.setTotalCount(new Long(totalCount));
            int totalPage = totalCount % count == 0 ? totalCount / count : totalCount / count + 1;
            page.setTotalPages(totalPage);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception( "查询新闻列表出错");
        }
    }

    @Override
    public Page<WebAuctionDTO>  getWebMeetList(Integer start, Integer count, String meetName, Integer meetState) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        map.put("meetName",meetName);
        map.put("meetState",meetState);
        try {
            List<WebAuctionDTO> list = meetMapper.getWebMeetList(start * count, count,meetName,meetState);
            List<WebAuctionDTO> dtoList = this.convertDTO(list);
            int totalCount = meetMapper.getWebMeetCount(map);
            Page<WebAuctionDTO> page = new Page<WebAuctionDTO>();
            page.setItems(dtoList);
            page.setPage(start + 1);
            page.setCount(count);
            page.setTotalCount(new Long(totalCount));
            int totalPage = totalCount % count == 0 ? totalCount / count : totalCount / count + 1;
            page.setTotalPages(totalPage);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception( "查询拍卖会列表出错");
        }
    }

    //首页展示拍卖会列表 正在进行>即将开始>已结束
    //根据当前时间和拍卖会的开始结束时间判断状态
    private List<WebAuctionDTO> convertDTO(List<WebAuctionDTO> list) {
        for (WebAuctionDTO webAuctionDTO : list) {
            Date nowTime = new Date();
            Meet meet = this.meetMapper.selectById(webAuctionDTO.getMeetId());
            if(webAuctionDTO.getStartTime().after(nowTime)){
                webAuctionDTO.setMeetState(1);
                meet.setMeetState(1);
            }
            if(webAuctionDTO.getStartTime().before(nowTime) && webAuctionDTO.getEndTime().after(nowTime)){
                webAuctionDTO.setMeetState(2);
                meet.setMeetState(2);
            }
            if(webAuctionDTO.getEndTime().before(nowTime)){
                webAuctionDTO.setMeetState(3);
                meet.setMeetState(3);
            }
            this.meetMapper.updateById(meet);
        }
        return list;
    }

    @Override
    public Page<WebAuctionDTO> getWebLotList(int start, int count,Integer sort, Long meetId) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        map.put("sort",sort);
        map.put("meetId",meetId);
        try {
            List<WebAuctionDTO> list = lotMapper.getWebLotList(start * count, count,sort,meetId);
            int totalCount = lotMapper.getWebLotCount(map);
            Page<WebAuctionDTO> page = new Page<WebAuctionDTO>();
            page.setItems(list);
            page.setPage(start + 1);
            page.setCount(count);
            page.setTotalCount(new Long(totalCount));
            int totalPage = totalCount % count == 0 ? totalCount / count : totalCount / count + 1;
            page.setTotalPages(totalPage);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception( "查询标的列表出错");
        }
    }

    @Override
    public List<WebAuctionDTO> getWebAllLotListByMeetId(Long meetId) throws Exception {
        try {
            return lotMapper.getWebAllLotListByMeetId(meetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception( "查询标的列表出错");
        }
    }

    @Override
    public Page<WebAuctionDTO> getWebNoticeList(int start, int count,String noticeTitle) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        map.put("noticeTitle",noticeTitle);
        try {
            List<WebAuctionDTO> list = noticeMapper.getWebNoticeList(start * count, count,noticeTitle);
            int totalCount = noticeMapper.getWebNoticeCount(map);
            Page<WebAuctionDTO> page = new Page<WebAuctionDTO>();
            page.setItems(list);
            page.setPage(start + 1);
            page.setCount(count);
            page.setTotalCount((long) totalCount);
            int totalPage = totalCount % count == 0 ? totalCount / count : totalCount / count + 1;
            page.setTotalPages(totalPage);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception( "查询拍卖公告列表出错");
        }
    }

    @Override
    public WebAuctionDTO getWebNoticeDetail(Long meetId) throws Exception {

        WebAuctionDTO noticeDetail = noticeMapper.getWebNoticeById(meetId);

        if (noticeDetail == null ) {
            throw new Exception("没有找到对应id的拍卖公告");
        }

        return noticeDetail;
    }
    @Override
    public void signUp(WebBidderDTO dto) throws Exception {
        if (null == dto) {
            throw new Exception("前台传递的数据异常");
        }
        //验证拍卖会相关
        Meet meet = this.usersInfoMapper.checkMeet(dto.getMeetId());
        if(null == meet){
            throw new Exception("该拍卖会不存在");
        }
        if(meet.getPublishState().equals(0)){
            throw new Exception("拍卖会未发布，新增拍卖会竞买人失败");
        }
        if(meet.getMeetState().equals(3)){
            throw new Exception("拍卖会已结束，不可报名");
        }
        if(meet.getMeetState().equals(2)){
            throw new Exception("拍卖会正在进行中，不可报名");
        }

        //验证注册用户信息表相关
        UsersInfo usersCheck = this.usersInfoMapper.selectById(dto.getBidderId());
        if(!usersCheck.getUsername().equals(dto.getBidderName()) && !usersCheck.getPhone().equals(dto.getPhone()) && !usersCheck.getIdCard().equals(dto.getPhone()))
        {
            throw new Exception("信息校验失败，请检查选项信息；如未注册，请先注册并实名认证后再报名！");
        }
/*
        UsersInfo checkBid = this.usersInfoMapper.checkBid(dto.getBidderName(),ValidateUtil.validateIdCard(dto.getIdCard()),ValidateUtil.validateMobile(dto.getPhone()));
*/

     /*   if(null == checkBid){
            throw new Exception("信息校验失败，请检查选项信息；如未注册，请先注册并实名认证后再报名！");
        }*/
        if(!usersCheck.getIsReal().equals(1)){
            throw new Exception("您未进行实名认证，请去网站完成实名认证后再报名！");
        }

        //开始新增
        Bidder bidder = new Bidder();
        Bidder bidderMeet = bidderMapper.getByMeetIdAndBidderId(dto.getMeetId(),usersCheck.getId());
        if(null != bidderMeet){
            throw new Exception("您以报名本场拍卖会！");
        }

        bidder.setBidderId(usersCheck.getId());
        bidder.setMeetId(dto.getMeetId());
        bidder.setBidderNum(BidUtil.genRandomNum());
        bidder.setCreateTime(new Date());
        this.bidderMapper.insert(bidder);
    }

    @Override
    public WebBidderDTO checkSignUp(Long meetId, Long bidderId) throws Exception {
        if(null == bidderId){
            throw new Exception("报名失败！请登录");
        }
        //验证拍卖会相关
        Meet meet = this.usersInfoMapper.checkMeet(meetId);
        if(null == meet){
            throw new Exception("该拍卖会不存在");
        }
        if(meet.getMeetState().equals(3)){
            throw new Exception("拍卖会已结束，不可报名");
        }
        if(meet.getDeadlineTime().equals(new Date())){
            throw new Exception("本场拍卖会以截止报名");
        }
        UsersInfo usersCheck = this.usersInfoMapper.selectById(bidderId);
        if(null == usersCheck){
            throw new Exception("报名失败！用户不存在");
        }
        if(usersCheck.getIsReal().equals(0)){
            throw new Exception("只有实名认证用户才可以报名拍卖会");
        }
        Bidder bidder = this.bidderMapper.getByMeetIdAndBidderId(meetId,bidderId);
        if(null != bidder){
            WebBidderDTO dto = new WebBidderDTO();
            dto.setBidderId(usersCheck.getId());
            return dto;
        }
        return null;

    }

    @Override
    public WebBidderDTO webCheckAndInfo(Long meetId, Long bidderId) throws Exception {
        if(null == bidderId){
            throw new Exception("报名失败！请登录");
        }
        //验证拍卖会相关
        Meet meet = this.usersInfoMapper.checkMeet(meetId);
        if(null == meet){
            throw new Exception("该拍卖会不存在");
        }
        if(meet.getMeetState().equals(3)){
            throw new Exception("拍卖会已结束，不可报名");
        }
        if(meet.getDeadlineTime().equals(new Date())){
            throw new Exception("本场拍卖会以截止报名");
        }
        UsersInfo usersCheck = this.usersInfoMapper.selectById(bidderId);
        if(null == usersCheck){
            throw new Exception("报名失败！用户不存在");
        }
        if(usersCheck.getIsReal().equals(0)){
            throw new Exception("只有实名认证用户才可以报名拍卖会");
        }
        Bidder bidder = this.bidderMapper.getByMeetIdAndBidderId(meetId,bidderId);
        if(null != bidder){
            throw new Exception("您已经报名过拍卖会！");
        }
        WebBidderDTO dto = new WebBidderDTO();
        dto.setBidderId(usersCheck.getId());
        dto.setBidderName(usersCheck.getUsername());
        dto.setIdCard(ValidateUtil.validateIdCard(usersCheck.getIdCard()));
        dto.setPhone(ValidateUtil.validateMobile(usersCheck.getPhone()));
        dto.setIsReal(usersCheck.getIsReal());
        dto.setMeetId(meet.getId());
        dto.setMeetName(meet.getName());

        MeetDTO notice = this.meetMapper.getNoticeTitleByMeetId(dto.getMeetId());
        dto.setNoticeTitle(notice.getNoticeTitle());
        return dto;
    }
}
