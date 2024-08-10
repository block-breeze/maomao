package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.ValidateUtil;
import com.auction.dto.*;
import com.auction.entity.BidDeal;
import com.auction.entity.Bidder;
import com.auction.entity.Lot;
import com.auction.entity.Meet;
import com.auction.mapper.BidDealMapper;
import com.auction.mapper.LotMapper;
import com.auction.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class WebController{
    private final static String lotMaxKey = "lot_max_";
    private final static String lotBidListKey = "lot_bid_list_";

    @Autowired
    UsersInfoService usersInfoService;
    @Autowired
    NewsService newsService;
    @Autowired
    SearchService searchService;
    @Autowired
    MeetService meetService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    BidderService bidderService;
    @Autowired
    LotService lotService;
    @Autowired
    BidDealService bidDealService;



    @Autowired
    LotMapper lotMapper;
    @Autowired
    BidDealMapper bidDealMapper;
    @Autowired
    @Qualifier("redisCache")
    private ICacheAccessor iCacheAccessor;

    /**
     * * 个人中心——实名认证
     * @param dto
     * @return
     */
    @RequestMapping(value = "/web/userCenter/real", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject webRealCertification(@RequestBody UsersInfoDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.usersInfoService.realCertification(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "实名认证成功，可以参与拍卖了");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 个人中心——账户安全——返回的用户信息
     * @param id
     * @return
     * @throws Exception
     */
  //  @RequestMapping(value = "/web/usersInfo/detail", method = RequestMethod.GET)
    @RequestMapping(value = "/web/userCenter/usersInfoDetail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUsersInfoDetail(@RequestParam("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            UsersInfoDTO dto = this.usersInfoService.selectById(id);
            if (dto.getIsReal().equals(1)){
                jsonObject.put("id", dto.getId());
                jsonObject.put("username", dto.getUsername());
                jsonObject.put("realName", dto.getRealName());
                jsonObject.put("phone", ValidateUtil.validateMobile(dto.getPhone()));
                jsonObject.put("sex", dto.getSex());
                jsonObject.put("e-mail", dto.getMail());
            }
            else if (dto.getIsReal().equals(0)){
                jsonObject.put("id", dto.getId());
                jsonObject.put("username", dto.getUsername());
                jsonObject.put("phone", ValidateUtil.validateMobile(dto.getPhone()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());

        }
        return jsonObject;
    }

    /**
     * 个人中心——我的竞拍——已报名
     * @param bidderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/userCenter/mySignUp", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject webMySignUpList(@RequestParam(value = "bidderId", required = false) Long bidderId) throws Exception {
        try {
            JSONObject result = this.bidderService.webGetMySignUpList(bidderId);
            result.put("code", "200");
            result.put("item", result.get("item"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询个人中心已报名拍卖会错误");
        }
    }

    /**
     * 个人中心——我的竞拍——已拍下
     * @param bidderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/userCenter/myBid", method = RequestMethod.GET)
    @ResponseBody
    public  JSONObject webMyBidList(@RequestParam(value = "bidderId", required = false) Long bidderId) throws Exception {
        try {
            JSONObject result = this.bidDealService.webGetMyBidList(bidderId);
            result.put("code", "200");
            result.get("item");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询个人中心已拍下标的错误");
        }
    }
    /**
     * 个人中心——我的竞拍——办理交割信息
     * @param bidderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/userCenter/checkPay", method = RequestMethod.GET)
    @ResponseBody
    public  JSONObject webCheckPay(@RequestParam("bidderId") Long bidderId,
                                   @RequestParam("lotId") Long lotId) throws Exception {
        try {
            return this.bidDealService.webCheckPay(bidderId,lotId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询个人中心已拍下标的错误");
        }
    }

    /**
     * 个人中心——我的竞拍——办理交割信息确认
     * @param bidderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/userCenter/Pay", method = RequestMethod.GET)
    @ResponseBody
    public  JSONObject webPay(@RequestParam("bidderId") Long bidderId,
                                   @RequestParam("lotId") Long lotId) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject();
            this.bidDealService.webPay(bidderId, lotId);
            jsonObject.put("code","200");
            jsonObject.put("msg","付款成功");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("付款失败");
        }
    }


    /**
     * 首页新闻资讯——导航
     * @param count
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/news/index", method = RequestMethod.GET)
    @ResponseBody
    public List<WebNewsDTO> webNewsIndex(@RequestParam("count") int count) throws Exception {
        try {
            return this.searchService.getWebNews(count);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询新闻资讯列表出错");
        }
    }

    /**
     * 首页新闻资讯——更多——进入新闻资讯列表
     *
     * @param start
     * @param count
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/news/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<WebNewsDTO> webNewsList(@RequestParam("start") int start, @RequestParam("count") int count) throws Exception {
        try {
            return searchService.getWebNewsList(start, count);
        } catch (Exception e) {
            throw new Exception("查询新闻动态资讯列表出错");
        }
    }

    /**
     * 首页新闻资讯——查询新闻详情
     */
    @RequestMapping(value = "/web/news/detail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject webNewsDetail(@RequestParam("id") Long id) throws Exception {

        try {
            newsService.newsReadAmount(id);
            NewsDTO dto = this.newsService.selectById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", dto.getId());
            jsonObject.put("title", dto.getTitle());
            jsonObject.put("state", dto.getState());
            jsonObject.put("content", dto.getContent());
            jsonObject.put("publishBy", dto.getPublishBy());
            jsonObject.put("publishTime", dto.getPublishTime());
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询新闻详情错误");
        }
    }




    /**
     * 首页拍卖会——拍卖会列表
     *
     * @param start
     * @param count
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/meet/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<WebAuctionDTO> webMeetList(@RequestParam("start") Integer start,
                                           @RequestParam("count") Integer count,
                                           @RequestParam(value = "meetName", required = false) String meetName,
                                           @RequestParam(value = "meetState", required = false) Integer meetState) throws Exception {
        try {
            return this.searchService.getWebMeetList(start, count, meetName, meetState);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询拍卖会列表出错");
        }
    }



    /**
     * 首页拍卖会——标的目录
     *
     * @param meetId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/meet/detail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject webMeetDetail(@RequestParam("meetId") Long meetId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            MeetDTO dto = this.meetService.selectById(meetId);

            jsonObject.put("id", dto.getId());
            jsonObject.put("name", dto.getName());
            jsonObject.put("startTime", dto.getStartTime());
            jsonObject.put("logo", dto.getLogo());
            jsonObject.put("linkMan", dto.getLinkMan());
            jsonObject.put("linkPhone", dto.getLinkPhone());
            jsonObject.put("lotCount", dto.getLotCount());
            jsonObject.put("meetState", dto.getMeetState());
            jsonObject.put("deadlineTime", dto.getDeadlineTime());

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());

        }
        return jsonObject;
    }


    /**
     * 首页拍卖会——拍卖大厅——查找所有标的
     * @param meetId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/lot/all/list", method = RequestMethod.GET)
    @ResponseBody
    public List<WebAuctionDTO> webLotList(@RequestParam(value = "meetId", required = false) Long meetId) throws Exception {
        try {
            return this.searchService.getWebAllLotListByMeetId(meetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询拍卖会列表出错");
        }
    }


    /**
     * 首页拍卖会——拍卖会详情——拍卖公告
     *
     * @param meetId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/meet/noticeDetail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject webNoticeDetail(@RequestParam("meetId") Long meetId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            WebAuctionDTO dto = this.searchService.getWebNoticeDetail(meetId);

            jsonObject.put("noticeId", dto.getNoticeId());
            jsonObject.put("noticeTitle", dto.getNoticeTitle());
            jsonObject.put("content", dto.getContent());
            jsonObject.put("publishBy",dto.getPublishBy());
            jsonObject.put("publishTime",dto.getPublishTime());
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());

        }
        return jsonObject;
    }


    /**
     * 首页拍卖会——拍卖会详情——标的目录 传meetId会按照展示顺序排
     * 首页标的——标的列表 传sort会按照id desc排序
     *
     * @param start
     * @param count
     * @param meetId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/lot/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<WebAuctionDTO> webLotList(@RequestParam("start") int start,
                                          @RequestParam("count") int count,
                                          @RequestParam(value = "sort", required = false) Integer sort,
                                          @RequestParam(value = "meetId", required = false) Long meetId) throws Exception {
        try {
            return this.searchService.getWebLotList(start, count, sort, meetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询拍卖会列表出错");
        }
    }


    /**
     * 首页拍卖公告——列表页
     *
     * @param start
     * @param count
     * @param noticeTitle
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/notice/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<WebAuctionDTO> webNoticeList(@RequestParam("start") int start,
                                             @RequestParam("count") int count,
                                             @RequestParam(value = "noticeTitle", required = false) String noticeTitle) throws Exception {
        try {
            return this.searchService.getWebNoticeList(start, count, noticeTitle);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询拍卖公告列表出错");
        }
    }

    /**
     * 首页拍卖公告——详情页
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/notice/detail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNoticeDetail(@RequestParam("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            NoticeDTO dto = this.noticeService.getNoticeDetailById(id);

            jsonObject.put("id", dto.getId());
            jsonObject.put("title", dto.getTitle());
            jsonObject.put("content", dto.getContent());
            jsonObject.put("publishBy", dto.getPublishBy());
            jsonObject.put("publishTime", dto.getPublishTime());

            jsonObject.put("meetId", dto.getMeetId());
            jsonObject.put("meetName", dto.getMeetName());
            jsonObject.put("startTime", dto.getStartTime());
            jsonObject.put("logo", dto.getLogo());
            jsonObject.put("meetState", dto.getMeetState());

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());

        }
        return jsonObject;
    }





    /**
     * 报名
     * 拍卖会详情页以及拍卖大厅————————点击报名按钮进行相关验证
     * @param meetId
     * @param bidderId
     * @throws Exception
     */
    @RequestMapping(value = "/web/bidder/webCheckSignUp", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject webCheckSignUp(@RequestParam("meetId") Long meetId,
                               @RequestParam("bidderId") Long bidderId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", "200");
            jsonObject.put("data", this.searchService.checkSignUp(meetId, bidderId));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 获取报名信息
     * @param meetId
     * @param bidderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/bidder/webCheckAndInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject webCheckAndInfo(@RequestParam("meetId") Long meetId,
                                     @RequestParam("bidderId") Long bidderId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", "200");
            jsonObject.put("data", this.searchService.webCheckAndInfo(meetId, bidderId));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 报名页面
     * @param dto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/web/bidder/signUp", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bidderSignUp(@RequestBody WebBidderDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {

            this.searchService.signUp(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "报名成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 报名
     * 验证竞买人是否报名拍卖会
     * @param meetId
     * @param bidderId
     * @return
     */
    @RequestMapping(value = "/web/bidder/checkSignUp", method = RequestMethod.GET)
    @ResponseBody
    public synchronized JSONObject checkSignUp(@RequestParam("bidderId") Long bidderId,
                                               @RequestParam("meetId") Long meetId) {
        JSONObject result = new JSONObject();
        try {
            Bidder bidder = bidderService.bidderInfoById(bidderId, meetId);
            result.put("code", "200");
            result.put("data", bidder);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "500");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 拍卖大厅——获取竞价记录
     * @param lotId
     * @return
     */
    @RequestMapping(value = "/web/bidder/bidList", method = RequestMethod.GET)
    @ResponseBody
    public synchronized JSONObject getBidList(@RequestParam("lotId") Long lotId) {
        JSONObject result = new JSONObject();
        try {
            ArrayList bidList = (ArrayList) iCacheAccessor.get(lotBidListKey + lotId);

            if (bidList != null) {
                HashMap map = (HashMap) bidList.get(bidList.size() - 1);
                BigDecimal nowPrice = new BigDecimal(map.get("nowPrice").toString());
                Long bidderId = Long.valueOf(map.get("bidderId").toString());
                String bidderNum = map.get("bidderNum").toString();

                Lot lot = this.lotService.lotInfoById(lotId);
                Meet meet = this.meetService.getMeetInfoById(lot.getMeetId());
                BidDeal checkBidDeal = this.bidDealService.dealInfoByLotId(lotId);
                if(null == checkBidDeal){
                    if(meet.getEndTime().before(new Date()) ){
                        if(!lot.getStartPrice().equals(nowPrice)){
                            BidDeal bidDeal = new BidDeal();
                            lot.setDealPrice(nowPrice);
                            lot.setAuctionStatus(1);
                            bidDeal.setIsPay(0);
                            bidDeal.setBidderId(bidderId);
                            bidDeal.setBidderNum(bidderNum);
                            bidDeal.setMeetId(meet.getId());
                            bidDeal.setDealPrice(nowPrice);
                            bidDeal.setLotId(lotId);
                            bidDeal.setLotName(lot.getName());
                            bidDeal.setCreateTime(new Date());
                            this.bidDealMapper.insert(bidDeal);
                            this.lotMapper.updateById(lot);
                        }
                    }
                }
            }
            result.put("code", "200");
            result.put("gg","成功");
            result.put("data", bidList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "500");
            result.put("msg", e.getMessage());
        }
        return result;
    }



    /**
     * 拍卖大厅——出价
     * 竞价
     * @param lotId
     * @param bidderId
     * @return
     */
    @RequestMapping(value = "/web/bidder/bidding", method = RequestMethod.GET)
    @ResponseBody
    public synchronized JSONObject bidding(@RequestParam("lotId") Long lotId,
                              @RequestParam("bidderId") Long bidderId,
                              @RequestParam("meetId") Long meetId) {
        JSONObject result = new JSONObject();
        try {
            //获取登录人信息
            Bidder bidder = bidderService.bidderInfoById(bidderId, meetId);
            if (bidder == null) {
                result.put("msg", "您未报名本场拍卖会，无法参与竞拍");
               return result;
            }

            Lot lot = this.lotService.lotInfoById(lotId);
            BigDecimal startPrice = lot.getStartPrice();
            BigDecimal increasePrice = lot.getIncreasePrice();

            //更新最高价信息
            HashMap maxInfo = this.updateOrCreateMaxInfoByRedis(startPrice, lotId, bidderId, increasePrice);
            BigDecimal nowPrice =  new BigDecimal(maxInfo.get("maxPrice").toString());

            //更新竞价列表
            ArrayList bidList = this.updateOrCreateBidListByRedis(lotId, bidderId, bidder.getBidderNum(), nowPrice);


            result.put("code", "200");
            result.put("msg", "出价成功");
            result.put("data", bidList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "500");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 更新或创建最高价信息
     * @param startPrice
     * @param lotId
     * @param bidderId
     * @param increasePrice
     * @return
     */
    private HashMap updateOrCreateMaxInfoByRedis(BigDecimal startPrice, Long lotId, Long bidderId, BigDecimal increasePrice) {
        HashMap maxInfoByRedis = (HashMap) iCacheAccessor.get(lotMaxKey + lotId);
        //如果还没创建过缓存
        if (maxInfoByRedis == null) {
            maxInfoByRedis = new HashMap();
            maxInfoByRedis.put("maxPrice", startPrice);
        }
        //更新最高价信息并存入缓存
        BigDecimal nowPrice = new BigDecimal(maxInfoByRedis.get("maxPrice").toString()).add(increasePrice);
        maxInfoByRedis.put("maxPrice", nowPrice);
        maxInfoByRedis.put("bidderId", bidderId);
        iCacheAccessor.set(lotMaxKey + lotId, maxInfoByRedis);
        return maxInfoByRedis;
    }

    /**
     * 更新或创建竞价列表
     * @param lotId
     * @param bidderId
     * @param bidderNum
     * @param nowPrice
     * @return
     */
    private ArrayList updateOrCreateBidListByRedis(Long lotId, Long bidderId, String bidderNum, BigDecimal nowPrice) throws Exception {
        ArrayList bidList = (ArrayList) iCacheAccessor.get(lotBidListKey + lotId);
        //如果还没有过竞价
        if (bidList == null) {
            bidList = new ArrayList<>();
        }
    /*    Lot lot = this.lotService.lotInfoById(lotId);
        Meet meet = this.meetService.getMeetInfoById(lot.getMeetId());
        if(meet.getEndTime().before(new Date()) ){
            if(!lot.getStartPrice().equals(nowPrice)){
                lot.setDealPrice(nowPrice);
                lot.setAuctionStatus(1);
                lot.setIsPay(0);
                lot.setBidderId(bidderId);
                lot.setUpdateTime(new Date());
            }
        }*/
        HashMap<String, Object> info = new HashMap<>();
        info.put("bidderId", bidderId);
        info.put("bidderNum", bidderNum);
        info.put("nowPrice", nowPrice);
        info.put("nowDate", new Date());
        bidList.add(info);
        iCacheAccessor.set(lotBidListKey + lotId, bidList);
        return bidList;
    }


}
