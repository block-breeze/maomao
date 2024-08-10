package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.NewsDTO;
import com.auction.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 新闻管理
 * JK17851192
 */
@RestController
public class NewsController {
    @Autowired
    NewsService newsService;

    @RequestMapping(value = "/sys/news/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<NewsDTO> newsList(@RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "publishBy", required = false) String publishBy,
                                  @RequestParam(value = "module", required = false) Integer module,
                                  @RequestParam(value = "state", required = false) Integer state,
                                  @RequestParam("start") int start, @RequestParam("count") int count,
                                  @RequestParam(value = "sortname", required = false) String sortname,
                                  @RequestParam(value = "sortorder", required = false) String sortorder) throws Exception {
        try {
            NewsDTO search = new NewsDTO(title, module,state,publishBy);
            return this.newsService.newsList(search, new PageSearch(start, count, sortname, sortorder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询列表出错");
        }
    }

    /**
     * 新闻新增
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/news/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject newsAdd(@RequestBody NewsDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.newsService.add(dto);
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
     * 新闻管理——编辑
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/news/modify", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject newsModify(@RequestBody NewsDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.newsService.modify(dto);
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
     * 新闻管理删除按钮
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/news/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject newsDelete(@PathVariable("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.newsService.delete(id);

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
     * 查询新闻详情
     */
    @RequestMapping(value = "/sys/news/detail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNewsDetail(@RequestParam("id") Long id) throws Exception {

        try {
            NewsDTO dto = this.newsService.selectById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",dto.getId());
            jsonObject.put("title",dto.getTitle());
            jsonObject.put("state",dto.getState());
            jsonObject.put("content",dto.getContent());
            jsonObject.put("publishBy",dto.getPublishBy());
            jsonObject.put("publishTime",dto.getPublishTime());
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询新闻详情错误");
        }
    }

    /**
     * 新闻管理——发布
     * @param id
     * @return
     */
    @RequestMapping(value = "/sys/news/publish", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject newsPublish(@RequestParam("id") Long id){
        JSONObject jsonObject = new JSONObject();
        try {
            this.newsService.publishById(id);
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
     * 新闻管理——提交
     * @param id
     * @return
     */
    @RequestMapping(value = "/sys/news/submit", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject newsSubmit(@RequestParam("id") Long id){
        JSONObject jsonObject = new JSONObject();
        try {
            this.newsService.submitById(id);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "新闻提交成功！");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }
    /**
     * 新闻管理——驳回
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/news/reject", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject newsReject(@RequestBody NewsDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.newsService.reject(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "新闻驳回成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

}
