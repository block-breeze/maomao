package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.LotDTO;
import com.auction.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LotController {

    @Autowired
    LotService lotService;

    /**
     * 标的管理——列表
     * 拍卖会管理——点击拍卖会显示关联的标的
     * @param name
     * @param meetName
     * @param meetId
     * @param auctionStatus
     * @param start
     * @param count
     * @param sortname
     * @param sortorder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/lot/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<LotDTO> LotList(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "meetName", required = false)  String meetName,
                                  @RequestParam(value = "meetId", required = false) Long meetId,
                                  @RequestParam(value = "auctionStatus", required = false) Integer auctionStatus,
                                  @RequestParam("start") int start,
                                  @RequestParam("count") int count,
                                  @RequestParam(value = "sortname", required = false) String sortname,
                                  @RequestParam(value = "sortorder", required = false) String sortorder) throws Exception {
        try {
            LotDTO search = new LotDTO(name,meetName,meetId,auctionStatus);
            return this.lotService.lotList(search, new PageSearch(start, count, sortname, sortorder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询标的列表出错");
        }
    }

    /**
     * 标的管理——修改
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/lot/modify", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject lotModify(@RequestBody LotDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.lotService.lotModify(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 标的管理——备注
     * @param dto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/lot/remarks", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject lotRemarks(@RequestBody LotDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.lotService.lotRemarks(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "备注成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 标的管理——根据id删除
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/lot/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject lotDelete(@PathVariable("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.lotService.lotDelete(id);

            jsonObject.put("code", "200");
            jsonObject.put("msg", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }


}
