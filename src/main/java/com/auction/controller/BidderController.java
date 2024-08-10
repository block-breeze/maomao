package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.BidderDTO;
import com.auction.service.BidderService;
import com.auction.service.UsersInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BidderController {
    @Autowired
    UsersInfoService usersInfoService;

    @Autowired
    BidderService bidderService;
    /**
     * 竞买人管理——竞买人列表
     * @param start
     * @param count
     * @param sortname
     * @param sortorder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/bidder/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<BidderDTO> bidderList(@RequestParam(value = "meetId") Long meetId,
                                         @RequestParam("start") int start, @RequestParam("count") int count,
                                      @RequestParam(value = "sortname", required = false) String sortname,
                                      @RequestParam(value = "sortorder", required = false) String sortorder) throws Exception {
        try {
            BidderDTO search = new BidderDTO(meetId);
            return this.bidderService.bidderList(search, new PageSearch(start, count, sortname, sortorder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询竞买人列表出错");
        }
    }

    /**
     * 竞买人管理——新增竞买人(后台报名)
     * @param dto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/bidder/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bidderAdd(@RequestBody BidderDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            dto.setBidderName(dto.getBidderName().trim());
            dto.setIdCard(dto.getIdCard().trim());
            dto.setPhone(dto.getPhone().trim());
            this.bidderService.add(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }
}
