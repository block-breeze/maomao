package com.auction.service.impl;


import com.auction.common.MD5Utils;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.dto.UserDTO;
import com.auction.entity.User;

import com.auction.mapper.UserMapper;

import com.auction.service.SearchService;
import com.auction.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * JPA+Mybatis-plus
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserMapper userMapper;
    @Autowired
    SearchService searchService;

    @Override
    public Page<UserDTO> userManagementList(UserDTO dto, PageSearch page) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        map.put("username", dto.getUsername());
        map.put("realName", dto.getRealName());

        int start = page.getStart()*page.getCount();
        map.put("sortorder", page.getSortorder());
        map.put("sortname", page.getSortname());
        map.put("start",start);
        map.put("count", page.getCount());
        Page<UserDTO> result = new Page<UserDTO>();
        int pageSize = page.getCount() < 1 ? 10 : page.getCount();
        int pageIndex = page.getStart() < 0 ? 0 : start / pageSize;
        Long totalCount = searchService.selectUserCount(map);
        List<UserDTO> list = searchService.selectUserList(map);
        List<UserDTO> dtoList = this.convertDTO(list);

        result.setCount(page.getCount());
        result.setPage(pageIndex + 1);
        result.setTotalCount(totalCount);
        result.setTotalPages(totalCount.intValue() / pageSize + 1);
        result.setItems(dtoList);
        return result;

    }

    private List<UserDTO> convertDTO(List<UserDTO> list) {
        List<UserDTO> dtoList = new ArrayList<>();
        for (UserDTO user : list) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setRealName(user.getRealName());
            dto.setPhone(user.getPhone());
            dto.setPerDescribe(user.getPerDescribe());
            dto.setType(user.getType());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public void userManagementAdd(UserDTO dto) throws Exception {
        logger.info("增加后台用户信息");
        if (null == dto) {
            throw new Exception("前台传递的数据异常");
        }
        User userlist = this.searchService.selectByUsername(dto.getUsername());
        if (null != userlist) {
            throw new Exception("该用户名已经存在");
        }
        //判断数据不为空
        if (null == dto.getUsername() || null == dto.getPassword() || null == dto.getRealName()||null == dto.getCheckPassword()||null == dto.getPhone()||null == dto.getType()) {
            throw new Exception("必填参数不得为空");
        }
        User user = new User();
        String password = MD5Utils.md5(dto.getPassword());
        String checkPassword = MD5Utils.md5(dto.getCheckPassword());
        if(!checkPassword.equals(password)){
            throw new Exception("两次密码输入不一致");
        }

        user.setUsername(dto.getUsername());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setPassword(password);
        //设置创建时间
        user.setCreateTime(new Date());
        //默认登陆身份为管理员
        user.setType(dto.getType());
        user.setPerDescribe(dto.getPerDescribe());

        this.save(user);
        logger.info("新增id为：" + user.getId());
    }

    @Override
    public void userManagementModify(UserDTO dto) throws Exception {
        logger.info("修改后台用户信息");
        if (null == dto || null == dto.getId()) {
            throw new Exception("数据异常！");
        }
        User user = userMapper.selectById(dto.getId());
        user.setUsername(dto.getUsername());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setType(dto.getType());
        user.setPerDescribe(dto.getPerDescribe());
        user.setUpdateTime(new Date());
        this.userMapper.updateById(user);

        logger.info("修改id为：" + user.getId());

    }

    @Override
    public void resetPassword(Long id,String oldPassword, String newPassword, String confirmPassword) throws Exception {
        logger.info("重置密码");
        User user = userMapper.selectById(id);
        if (null == user) {
            throw new Exception("数据异常");
        }
        String passwordEncode = MD5Utils.md5(oldPassword);
        if (!user.getPassword().equals(passwordEncode)) {
            throw new Exception("旧密码输入错误!");
        }
        if (null == newPassword) {
            throw new Exception("新密码不允许为空!");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("两次输入密码不一致!");
        }
        String password = MD5Utils.md5(newPassword);
        user.setPassword(password);
        this.userMapper.updateById(user);
        logger.info("重置密码的id为：" + user.getId());
    }

    @Override
    public void userManagementDelete(Long id) throws Exception {
        logger.info("删除后台用户信息");
        if (null == id) {
            throw new Exception("数据异常！");
        }
        User user = userMapper.selectById(id);
        if (null == user) {
            throw new Exception("此信息不存在！");
        }
        this.userMapper.deleteById(id);

        logger.info("删除id为：" + user.getId());
    }

}
