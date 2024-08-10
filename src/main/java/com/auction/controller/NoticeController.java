package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.NoticeDTO;
import com.auction.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zaria
 * @Date 2021/3/27
 * JK17851192
 */
@RestController
public class NoticeController{
    @Autowired
    NoticeService noticeService;

    /**
     * 拍卖公告——列表
     * @param title
     * @param state
     * @param start
     * @param count
     * @param sortname
     * @param sortorder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/notice/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<NoticeDTO> noticeList(@RequestParam(value = "title", required = false) String title,
                                              @RequestParam(value = "state", required = false) Integer state,
                                              @RequestParam("start") int start, @RequestParam("count") int count,
                                      @RequestParam(value = "sortname", required = false) String sortname,
                                      @RequestParam(value = "sortorder", required = false) String sortorder) throws Exception {
        try {
            NoticeDTO search = new NoticeDTO(title,state);
            return this.noticeService.noticeList(search, new PageSearch(start, count, sortname, sortorder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询列表出错");
        }
    }

    /**
     * 拍卖公告新增
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/notice/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject noticeAdd(@RequestBody NoticeDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.noticeService.noticeAdd(dto);
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
     * 拍卖公告修改
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/notice/modify", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject noticeModify(@RequestBody NoticeDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.noticeService.noticeModify(dto);
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
     * 拍卖公告删除按钮
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/notice/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject noticeDelete(@PathVariable("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.noticeService.noticeDelete(id);

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
     * 查询拍卖公告详情
     */
    @RequestMapping(value = "/sys/notice/detail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNoticeDetail(@RequestParam("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            NoticeDTO dto = this.noticeService.getNoticeDetailById(id);

            jsonObject.put("id", dto.getId());
            jsonObject.put("title", dto.getTitle());
            jsonObject.put("state", dto.getState());
            jsonObject.put("content", dto.getContent());
            jsonObject.put("publishBy", dto.getPublishBy());
            jsonObject.put("publishTime", dto.getPublishTime());
            jsonObject.put("meetName", dto.getMeetName());
            jsonObject.put("startTime", dto.getStartTime());
            jsonObject.put("logo", dto.getLogo());


        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());

        }
        return jsonObject;
    }
    /**
     * 拍卖公告——发布
     * @param id
     * @return
     */
    @RequestMapping(value = "/sys/notice/publish", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject noticePublish(@RequestParam("id") Long id){
        JSONObject jsonObject = new JSONObject();
        try {
            this.noticeService.publishById(id);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "发布公告成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 拍卖公告——撤销
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/notice/reject", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject noticeReject(@RequestBody NoticeDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.noticeService.reject(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "撤销公告成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 拍卖公告关联
     * @param id
     * @return
     */
    @RequestMapping(value = "/sys/notice/relation", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject noticeRelation(@RequestParam("id") Long id) {
        JSONObject jsonObject = new JSONObject();

        try {
            JSONObject jo = this.noticeService.relationById(id);
            jsonObject.put("code", "200");
            jsonObject.put("data", jo);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 取消关联拍卖会
     * @param id
     * @return
     */

    @RequestMapping(value = "/sys/notice/cancelRelation", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject noticeCancelRelation(@RequestParam("id") Long id) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.noticeService.cancelRelationById(id);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "取消关联拍卖会成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }



}
