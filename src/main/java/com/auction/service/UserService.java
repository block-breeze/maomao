package com.auction.service;

import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.UserDTO;


public interface UserService {

    public Page<UserDTO> userManagementList(UserDTO dto, PageSearch page) throws Exception;


    //新增后台登录用户
    public void userManagementAdd(UserDTO dto) throws Exception;
    //后台用户信息修改
    public void userManagementModify(UserDTO dto) throws Exception;

    public void resetPassword(Long id,String oldPassword, String newPassword, String confirmPassword) throws Exception;

    public void userManagementDelete(Long id) throws Exception;
}
