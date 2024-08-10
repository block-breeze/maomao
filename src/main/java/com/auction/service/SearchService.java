package com.auction.service;

import com.auction.common.Page;
import com.auction.dto.*;
import com.auction.entity.User;

import java.util.List;
import java.util.Map;

public interface SearchService {
    /**
     * 用户管理
     */
    Long selectUserCount(Map<Object, Object> map);

    List<UserDTO> selectUserList(Map<Object, Object> map);

    User selectByUsername(String username);

    /**
     * 新闻管理
     */

    Long selectNewsCount(Map<Object, Object> map);

    List<NewsDTO> selectNewsList(Map<Object, Object> map);


    /**
     * 拍卖公告管理
     */

    Long selectNoticeCount(Map<Object, Object> map);

    List<NoticeDTO> selectNoticeList(Map<Object, Object> map);

    /**
     * 拍卖会管理
     * @param map
     * @return
     */
    Long selectMeetCount(Map<Object, Object> map);

    List<MeetDTO> selectMeetList(Map<Object, Object> map);

    /**
     * 标的管理
     * @param map
     * @return
     */
    Long selectLotCount(Map<Object, Object> map);

    List<LotDTO> selectLotList(Map<Object, Object> map);

    /**
     * 注册用户管理
     * @param map
     * @return
     */
    Long selectUsersInfoCount(Map<Object, Object> map);

    List<UsersInfoDTO> selectUsersInfoList(Map<Object, Object> map);

    /**
     * 竞买人列表
     * @param map
     * @return
     */
    Long selectBidderCount(Map<Object, Object> map);

    List<BidderDTO> selectBidderList(Map<Object, Object> map);


    /**
     * 前台页面查询接口
     */
    List<WebNewsDTO> getWebNews(int count) throws Exception;

    Page<WebNewsDTO> getWebNewsList(int start, int count) throws Exception;

    Page<WebAuctionDTO> getWebMeetList(Integer start, Integer count, String meetName, Integer meetState) throws Exception;

    Page<WebAuctionDTO> getWebLotList(int start, int count, Integer sort, Long meetId) throws Exception;

    List<WebAuctionDTO> getWebAllLotListByMeetId(Long meetId) throws Exception;

    Page<WebAuctionDTO> getWebNoticeList(int start, int count ,String noticeTitle) throws Exception;
    //拍卖公告详情接口
    WebAuctionDTO getWebNoticeDetail(Long meetId) throws Exception;
    //报名页面
    public void signUp(WebBidderDTO dto) throws Exception;

    /*JSONObject checkSignUp(WebBidderDTO dto) throws Exception;*/

    WebBidderDTO checkSignUp(Long meetId, Long bidderId) throws Exception;

    WebBidderDTO webCheckAndInfo(Long meetId, Long bidderId) throws Exception;
}
