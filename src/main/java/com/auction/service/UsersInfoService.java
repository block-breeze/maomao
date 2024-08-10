package com.auction.service;


import com.alibaba.fastjson.JSONObject;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.PasswordDTO;
import com.auction.dto.UsersInfoDTO;

public interface UsersInfoService {
    public Page<UsersInfoDTO> usersInfoList(UsersInfoDTO dto, PageSearch page) throws Exception;

    public void register(UsersInfoDTO dto) throws Exception;

    public void login(UsersInfoDTO dto) throws Exception;

    public String  adminReset(Long id, String newPassword, String confirmPassword) throws Exception;

    /**
     * 个人中心——身份信息展示
     * 后台——注册用户详情接口
     * @param id
     * @return
     * @throws Exception
     */

    UsersInfoDTO selectById (Long id) throws Exception;

    //个人中心——实名
    public void realCertification(UsersInfoDTO dto) throws Exception;

    //登录页面——忘记密码
    public void forgetPassword(PasswordDTO dto) throws Exception;

    //登录页面——忘记密码
    public JSONObject checkUsername(String username) throws Exception;
}
