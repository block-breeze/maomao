package com.auction.controller;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.UsersInfoDTO;
import com.auction.service.UsersInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersInfoController {

    @Autowired
    UsersInfoService usersInfoService;

    /**
     * 后台查看注册用户列表
     * @param username
     * @param realName
     * @param isReal
     * @param start
     * @param count
     * @param sortname
     * @param sortorder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/usersInfo/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<UsersInfoDTO> usersInfoList(@RequestParam(value = "username", required = false) String username,
                                            @RequestParam(value = "realName", required = false) String realName,
                                            @RequestParam(value = "isReal", required = false) Integer isReal,
                                            @RequestParam("start") int start, @RequestParam("count") int count,
                                            @RequestParam(value = "sortname", required = false) String sortname,
                                            @RequestParam(value = "sortorder", required = false) String sortorder) throws Exception {
        try {
            UsersInfoDTO search = new UsersInfoDTO(username, realName,isReal);
            return this.usersInfoService.usersInfoList(search, new PageSearch(start, count, sortname, sortorder));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询列表出错");
        }
    }

    /**
     * 管理员重置密码
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/sys/usersInfo/adminReset", method = RequestMethod.PUT)
    public JSONObject adminReset(@RequestBody JSONObject jsonObject) {
        Long id = jsonObject.getLong("id");
        String newPassword = jsonObject.getString("newPassword");
        String confirmPassword = jsonObject.getString("confirmPassword");
        try {
            this.usersInfoService.adminReset(id,newPassword,confirmPassword);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "管理员重置密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 后台查看竞买人信息——详情
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sys/usersInfo/detail", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUsersInfoDetail(@RequestParam("id") Long id) throws Exception {
        JSONObject result = new JSONObject();
        try {
            UsersInfoDTO dto = this.usersInfoService.selectById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("isReal", dto.getIsReal());
            jsonObject.put("id", dto.getId());
            jsonObject.put("username", dto.getUsername());
            jsonObject.put("realName", dto.getRealName());
            jsonObject.put("phone", dto.getPhone());
            jsonObject.put("mail", dto.getMail());
            jsonObject.put("idCard", dto.getIdCard());

            jsonObject.put("createTime", dto.getCreateTime());
            jsonObject.put("idBack", dto.getIdBack());
            jsonObject.put("idFront", dto.getIdFront());
            result.put("data",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", "500");
            result.put("msg", e.getMessage());

        }
        return result;
    }



}
