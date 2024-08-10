package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.UserDTO;
import com.auction.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 * JK17851192
 * mybatis-plus
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    public UserController() {
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 用户管理列表
     *
     * @param username
     * @param realName
     * @param start 分页参数 从0页开始
     * @param count 分页参数 ，每页多少个
     * @param sortname 排序参数
     * @param sortorder 排序条件 asc desc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/userManagement/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<UserDTO> userManagementList(@RequestParam(value = "username", required = false) String username,
                                            @RequestParam(value = "realName", required = false) String realName,
                                            @RequestParam("start") int start, @RequestParam("count") int count,
                                            @RequestParam(value = "sortname", required = false) String sortname,
                                            @RequestParam(value = "sortorder", required = false) String sortorder) throws Exception {
        try {
            UserDTO search = new UserDTO(username, realName);
            return this.userService.userManagementList(search, new PageSearch(start, count, sortname, sortorder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询列表出错");
        }
    }

    /**
     * 新增后台登录用户
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/userManagement/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject userManagementAdd(@RequestBody UserDTO dto) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.userService.userManagementAdd(dto);
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
     * 编辑后台用户
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/userManagement/modify", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject userManagementModify(@RequestBody UserDTO dto) {
        JSONObject jsonObject = new JSONObject();

        try {
            this.userService.userManagementModify(dto);
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
     * 用户管理重置密码
     * @param jo
     * @return
     */
    @RequestMapping(value = "/sys/userManagement/reset", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject resetPassword(@RequestBody JSONObject jo) {
        Long id = jo.getLong("id");
       String oldPassword = jo.getString("oldPassword");
        String newPassword = jo.getString("newPassword");
        String confirmPassword = jo.getString("confirmPassword");
        JSONObject jsonObject = new JSONObject();
        try {
           this.userService.resetPassword(id, oldPassword, newPassword, confirmPassword);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "重置密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 用户管理删除按钮
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/userManagement/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject userManagementDelete(@PathVariable("id") Long id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            this.userService.userManagementDelete(id);

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
     * 获取
     * @return userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * 设置
     * @param userService
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String toString() {
        return "UserController{userService = " + userService + "}";
    }
}
