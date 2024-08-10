package com.auction.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.auction.common.MD5Utils;
import com.auction.dto.UserDTO;
import com.auction.dto.UsersInfoDTO;
import com.auction.entity.User;
import com.auction.entity.UsersInfo;
import com.auction.mapper.LoginMapper;
import com.auction.mapper.UserMapper;
import com.auction.mapper.UsersInfoMapper;
import com.auction.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, User> implements LoginService {

    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    LoginMapper loginMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UsersInfoMapper usersInfoMapper;


    @Override
    public void login(UserDTO dto) throws Exception {
        logger.info("后台用户正在进行登录······");
        if(null == dto){
            throw new Exception("登录数据不能为空");
        }
        if(null == dto.getUsername()){
            throw new Exception("用户名不能为空");
        }
        if(null == dto.getPassword()){
            throw new Exception("密码不能为空");
        }
        User user = userMapper.selectByUsername(dto.getUsername());
        if(null == user){
            throw new Exception("该用户不存在，请检查后登录");
        }else {
            String userPassword = MD5Utils.md5(dto.getPassword());//用户输入密码进行加密
            if(!userPassword.equals(user.getPassword())){
                throw new Exception("密码输入错误，请重新输入");
            }
        }
        logger.info("用户登录成功，用户id为："+user.getId()+"登陆时间："+new Date());


    }

    @Override
    public JSONObject getSysUserInfo(UserDTO dto) throws Exception {
        if(null == dto){
            throw new Exception("数据不能为空");
        }
        if(null == dto.getUsername()){
            throw new Exception("用户名不能为空");
        }
        User user = userMapper.selectByUsername(dto.getUsername());
        if(null == user){
            throw new Exception("该用户不存在，请检查");
        }
        JSONObject jo = new JSONObject();
        jo.put("id",user.getId());
        jo.put("username",user.getUsername());
        jo.put("phone",user.getPhone());
        jo.put("type",user.getType());
        return jo;

    }

    @Override
    public JSONObject getUsersInfo(UsersInfoDTO dto) throws Exception {

        if(null == dto){
            throw new Exception("数据不能为空");
        }
        if(null == dto.getUsername()){
            throw new Exception("用户名不能为空");
        }

        UsersInfo usersInfo = usersInfoMapper.selectByUsername(dto.getUsername());
        if(null == usersInfo){
            throw new Exception("该用户不存在，请检查");
        }
        JSONObject jo = new JSONObject();
        jo.put("id",usersInfo.getId());
        if(dto.getUsername().equals(usersInfo.getUsername())){
            jo.put("username",usersInfo.getUsername());
        }
        else if(dto.getUsername().equals(usersInfo.getPhone())) {
            jo.put("username",usersInfo.getPhone());
        }
        jo.put("isReset",usersInfo.getIsAdminReset());
        jo.put("isReal",usersInfo.getIsReal());
        return jo;
    }


}
