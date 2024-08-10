package com.auction.controller;

import com.alibaba.fastjson.JSONObject;

import com.auction.dto.PasswordDTO;
import com.auction.dto.UserDTO;
import com.auction.dto.UsersInfoDTO;
import com.auction.service.LoginService;
import com.auction.service.UsersInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    UsersInfoService usersInfoService;

    /**
     * 后台用户登录
     * @param dto
     * @return
     */
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public JSONObject loginBack(@RequestBody UserDTO dto) {
        JSONObject jsonObject = new JSONObject();
        try {
            this.loginService.login(dto);

            jsonObject.put("code", "200");
            jsonObject.put("msg", "后台用户登陆成功");
            jsonObject.put("data",this.loginService.getSysUserInfo(dto));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 前台用户注册
     * @param dto
     * @return
     */
    @RequestMapping(value = "/web/usersInfo/register", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject register(@RequestBody UsersInfoDTO dto) {
        JSONObject jsonObject = new JSONObject();
        try {
            this.usersInfoService.register(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "注册成功");
            jsonObject.put("data", this.loginService.getUsersInfo(dto));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 前台用户登录
     * @param dto
     * @return
     */
    @RequestMapping(value = "/web/usersInfo/login", method = RequestMethod.POST)
    public JSONObject login(@RequestBody UsersInfoDTO dto) {
        JSONObject jsonObject = new JSONObject();
        try {
            this.usersInfoService.login(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "前台登陆成功");
            jsonObject.put("data", this.loginService.getUsersInfo(dto));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 忘记密码
     * @param dto
     * @return
     */
    @RequestMapping(value = "/web/usersInfo/forgetPassword", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject forgetPassword(@RequestBody PasswordDTO dto) {
        JSONObject jsonObject = new JSONObject();
        try {
            this.usersInfoService.forgetPassword(dto);
            jsonObject.put("code", "200");
            jsonObject.put("msg", "忘记密码操作——重置密码成功");
           // jsonObject.put("data", this.loginService.getUsersInfo(dto));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }


    /**
     * 验证用户名是否存在
     * @return
     */
    @RequestMapping(value = "/web/usersInfo/checkUsername", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject checkUsername(@RequestParam(value = "username") String username) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("code", "200");
            jsonObject.put("msg", "验证成功");
            jsonObject.put("data", this.usersInfoService.checkUsername(username));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", e.getMessage());
        }
        return jsonObject;
    }

}
