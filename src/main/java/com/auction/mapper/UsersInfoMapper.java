package com.auction.mapper;


import com.auction.dto.UsersInfoDTO;
import com.auction.entity.Meet;
import com.auction.entity.UsersInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UsersInfoMapper extends BaseMapper<UsersInfo> {

    //查询用户表总数
    Long selectUsersInfoCount(Map<Object, Object> map);
    //查询用户表
    List<UsersInfoDTO> selectUsersInfoList(Map<Object, Object> map);

    UsersInfo  selectByUsername(String username);

    UsersInfo selectByUsernameOrPhone(String username);

    UsersInfo checkBid(@Param("username") String bidderName, @Param("idCard") String idCard,@Param("phone")  String phone);

    UsersInfo  selectByPhone(String phone);

    Meet checkMeet(Long meetId);



}
