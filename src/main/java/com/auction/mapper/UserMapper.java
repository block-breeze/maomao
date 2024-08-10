package com.auction.mapper;

import com.auction.dto.UserDTO;
import com.auction.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    //查询用户表总数
    Long selectUserCount(Map<Object, Object> map);
    //查询用户表
    List<UserDTO> selectUserList(Map<Object, Object> map);

    User selectByUsername(String username);
}
