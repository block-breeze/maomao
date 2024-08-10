package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.LotDTO;
import com.auction.dto.MeetDTO;
import com.auction.service.LotService;
import com.auction.service.MeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author zaria
 * @Date 2021/3/28
 */
@RestController
public class MeetController{
    @Autowired
    MeetService meetService;
    @Autowired
    LotService lotService;

    /**
     * 拍卖会管理——列表
     * @param name
     * @param meetState
     * @param publishState
     * @param start
     * @param count
     * @param sortname
     * @param sortorder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/meet/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<MeetDTO> meetList(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "meetState", required = false) Integer meetState,
                                            @RequestParam(value = "publishState", required = false) Integer publishState,
                                            @RequestParam("start") int start, @RequestParam("count") int count,
                                  @RequestParam(value = "sortname", required = false) String sortname,
                                  @RequestParam(value = "sortorder", required = false) String sortorder) throws Exception {
        try {
            MeetDTO search = new MeetDTO(name,meetState,publishState);
            return this.meetService.meetList(search, new PageSearch(start, count, sortname, sortorder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询拍卖会列表出错");
        }
    }

    /**
     * 拍卖会管理 新增
     * @param dto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/meet/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject meetAdd(@RequestBody MeetDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.meetService.meetAdd(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 拍卖会管理——修改
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/meet/modify", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject meetModify(@RequestBody MeetDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.meetService.meetModify(dto);
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
     * 拍卖会管理——根据id删除
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/meet/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject meetDelete(@PathVariable("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.meetService.meetDelete(id);

            jsonObject.put("code", "200");
            jsonObject.put("msg", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 拍卖会详情
     * 后台拍卖会管理——查看
     * 前台标的目录
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/meet/detail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getMeetDetail(@RequestParam("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            MeetDTO dto = this.meetService.selectById(id);

            jsonObject.put("id", dto.getId());
            jsonObject.put("name", dto.getName());
            jsonObject.put("startTime", dto.getStartTime());
            jsonObject.put("logo", dto.getLogo());
            jsonObject.put("linkMan", dto.getLinkMan());
            jsonObject.put("linkPhone", dto.getLinkPhone());
            jsonObject.put("lotCount", dto.getLotCount());

        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());

        }
        return jsonObject;
    }


    /**
     * 拍卖会管理 ——新增拍品
     * @param dto
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/meet/addLot", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject lotAdd(@RequestBody LotDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.lotService.lotAdd(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "关联标的成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 根据公告标题查公告
     * @param noticeTitle
     * @return
     */
    @RequestMapping(value = "/sys/meet/noticesListTen", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> noticesListTen(@RequestParam(value = "noticeTitle", required = false) String noticeTitle) {

        return this.meetService.noticesListTen("*".equals(noticeTitle) ? null : noticeTitle);
    }
}
