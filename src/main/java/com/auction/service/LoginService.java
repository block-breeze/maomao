package com.auction.service;


import com.alibaba.fastjson.JSONObject;
import com.auction.dto.UserDTO;
import com.auction.dto.UsersInfoDTO;
import com.auction.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LoginService extends IService<User> {


    public void login(UserDTO dto) throws Exception;


    //前台登录返回用户信息
    public JSONObject getUsersInfo(UsersInfoDTO dto) throws Exception;
    //后台登录返回用户信息
    public JSONObject getSysUserInfo(UserDTO dto) throws Exception;


}
