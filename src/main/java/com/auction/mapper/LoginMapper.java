package com.auction.mapper;
import com.auction.entity.User;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginMapper extends BaseMapper<User> {
    List<User> queryByUserName(String username);

}
