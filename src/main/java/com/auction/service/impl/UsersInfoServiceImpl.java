package com.auction.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.auction.common.MD5Utils;
import com.auction.common.Page;
import com.auction.common.PageSearch;
import com.auction.common.ValidateUtil;
import com.auction.dto.PasswordDTO;
import com.auction.dto.UsersInfoDTO;
import com.auction.entity.UsersInfo;
import com.auction.mapper.UsersInfoMapper;
import com.auction.service.LoginService;
import com.auction.service.SearchService;
import com.auction.service.UsersInfoService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.auction.common.ValidateUtil.validateMobile;

@Service
public class UsersInfoServiceImpl extends ServiceImpl<UsersInfoMapper, UsersInfo> implements UsersInfoService {
    Logger logger = LoggerFactory.getLogger(UsersInfoServiceImpl.class);

    @Autowired
    SearchService searchService;

    @Autowired
    UsersInfoMapper usersInfoMapper;

    @Autowired
    LoginService loginService;


    @Override
    public Page<UsersInfoDTO> usersInfoList(UsersInfoDTO dto, PageSearch page) throws Exception {
        Map<Object, Object> map = new HashMap<>();
        //注册用户列表查询
        map.put("username", dto.getUsername());
        map.put("realName", dto.getRealName());
        map.put("isReal", dto.getIsReal());


        int start = page.getStart()*page.getCount();

        map.put("sortorder", page.getSortorder());
        map.put("sortname", page.getSortname());
        map.put("start", start);
        map.put("count", page.getCount());
        Page<UsersInfoDTO> result = new Page<UsersInfoDTO>();
        int pageSize = page.getCount() < 1 ? 10 : page.getCount();
        int pageIndex = page.getStart() < 0 ? 0 : start / pageSize;
        Long totalCount = searchService.selectUsersInfoCount(map);
        List<UsersInfoDTO> list = searchService.selectUsersInfoList(map);
        List<UsersInfoDTO> dtoList = this.convertDTO(list);

        result.setCount(page.getCount());
        result.setPage(pageIndex + 1);
        result.setTotalCount(totalCount);
        result.setTotalPages(totalCount.intValue() / pageSize + 1);
        result.setItems(dtoList);
        return result;
    }
    private List<UsersInfoDTO> convertDTO(List<UsersInfoDTO> list) {
        for (UsersInfoDTO user : list) {
            user.setPhone(validateMobile(user.getPhone()));
        }
        return list;
    }


    @Override
    public void register(UsersInfoDTO dto) throws Exception {
        logger.info("有新用户正在进行注册······");
        if(null == dto){
            throw new Exception("注册信息不能为空！");
        }
        UsersInfo usersByUsername = this.usersInfoMapper.selectByUsername(dto.getUsername());
        if(null != usersByUsername){
            throw new Exception("该用户名已经存在！");
        }
        UsersInfo usersByPhone =this.usersInfoMapper.selectByPhone(dto.getPhone());
        if(null != usersByPhone){
            throw new Exception("该手机号已经注册！");
        }
        if(null == dto.getPhone()){
            throw new Exception("手机号不得为空！");
        }
        if(null == dto.getPassword()){
            throw new Exception("账号密码不得为空！");
        }
        UsersInfo usersInfo = new UsersInfo();
        if(null != dto.getUsername()){
            usersInfo.setUsername(dto.getUsername());
        }else{
            usersInfo.setUsername(dto.getPhone());
        }
        usersInfo.setPhone(dto.getPhone());
        String passwordEncode = MD5Utils.md5(dto.getPassword());
        usersInfo.setPassword(passwordEncode);
        usersInfo.setIsReal(0);
        usersInfo.setIsAdminReset(0);
        usersInfo.setCreateTime(new Date());
        this.save(usersInfo);
        logger.info("平台有新用户注册，注册id为：" + usersInfo.getId());
    }

    @Override
    public void login(UsersInfoDTO dto) throws Exception {
        logger.info("用户正在进行登录······");
        if(null == dto){
            throw new Exception("登录数据不能为空");
        }
        if(null == dto.getUsername()){
            throw new Exception("用户名不能为空");
        }
        if(null == dto.getPassword()){
            throw new Exception("密码不能为空");
        }
        UsersInfo usersInfo = usersInfoMapper.selectByUsername(dto.getUsername());
        if(null == usersInfo){
            throw new Exception("该用户不存在，请检查后登录");
        }else {
            String userPassword = MD5Utils.md5(dto.getPassword());//用户输入密码进行加密
            if(!userPassword.equals(usersInfo.getPassword())){
                throw new Exception("密码输入错误，请重新输入");
            }
        }
        logger.info("用户登录成功，用户id为："+usersInfo.getId()+"登陆时间："+new Date());
    }

    @Override
    public String adminReset(Long id, String newPassword, String confirmPassword) throws Exception {
        logger.info("管理员重置密码");
        UsersInfo usersInfo = this.getById(id);
        if (null == usersInfo) {
            throw new Exception("该用户不存在");
        }

  /*       WebUserDTO dto = loginService.getSysUserInfo(dto)
        //验证当前登录用户的type是不是2
        JSONObject jo = ;

        UsersInfoDTO dto  = usersInfoMapper.getAdminType()
*/
        String password = MD5Utils.md5(newPassword);
        usersInfo.setPassword(password);
        usersInfo.setIsAdminReset(1);
        usersInfo.setUpdateTime(new Date());

        this.updateById(usersInfo);
        logger.info("管理员"+"重置密码的id为：" + usersInfo.getId());
        return usersInfo.getUsername();
    }

    @Override
    public UsersInfoDTO selectById(Long id) throws Exception {
        UsersInfo usersInfo = this.getById(id);

        if (usersInfo == null ) {
            throw new Exception("没有找到该用户信息");
        }
        UsersInfoDTO dto = new UsersInfoDTO();
        if(usersInfo.getIsReal().equals(1)){
            dto.setIsReal(usersInfo.getIsReal());
            dto.setId(usersInfo.getId());
            dto.setUsername(usersInfo.getUsername());
            dto.setRealName(usersInfo.getRealName());
            dto.setPhone(ValidateUtil.validateMobile(usersInfo.getPhone()));
            dto.setMail(usersInfo.getMail());
            dto.setIdCard(ValidateUtil.validateIdCard(usersInfo.getIdCard()));
            dto.setCreateTime(usersInfo.getCreateTime());
            dto.setIdBack(usersInfo.getIdBack());
            dto.setIdFront(usersInfo.getIdFront());
            dto.setSex(usersInfo.getSex());
        }
        else if(usersInfo.getIsReal().equals(0)){
            dto.setIsReal(usersInfo.getIsReal());
            dto.setId(usersInfo.getId());
            dto.setUsername(usersInfo.getUsername());
            dto.setPhone(ValidateUtil.validateMobile(usersInfo.getPhone()));
            dto.setCreateTime(usersInfo.getCreateTime());
        }
        return dto;
    }


    @Override
    public void realCertification(UsersInfoDTO dto) throws Exception {
        if (null == dto) {
            throw new Exception("不能提交空数据！");
        }
           UsersInfo usersInfo = this.getById(dto.getId());
        if(null == usersInfo){
            throw new Exception("该用户不存在！");
        }
        usersInfo.setRealName(dto.getRealName());
        usersInfo.setIdCard(dto.getIdCard());
        usersInfo.setIdFront(dto.getIdFront());
        usersInfo.setIdBack(dto.getIdBack());
        usersInfo.setMail(dto.getMail());
        usersInfo.setSex(dto.getSex());
        usersInfo.setIsReal(1);
        usersInfo.setUpdateTime(new Date());
        this.usersInfoMapper.updateById(usersInfo);
        logger.info("用户正在进行实名认证");
    }

    @Override
    public void forgetPassword(PasswordDTO dto) throws Exception {
        logger.info("忘记密码");
        UsersInfo usersInfo = usersInfoMapper.selectByUsernameOrPhone(dto.getUsername());
        if (null == usersInfo) {
            throw new Exception("您输入的用户名或者手机号有误,请检查后重新输入");
        }

        //感觉要加判断条件不知道加什么
        if (null == dto.getNewPassword()) {
            throw new Exception("新密码不允许为空!");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new Exception("两次输入密码不一致!");
        }
        String password = MD5Utils.md5(dto.getNewPassword());
        usersInfo.setPassword(password);
        usersInfo.setUpdateTime(new Date());
        usersInfo.setIsAdminReset(0);
        this.usersInfoMapper.updateById(usersInfo);
        logger.info("重置密码的id为：" + usersInfo.getId());
    }

    @Override
    public JSONObject checkUsername(String username) throws Exception {
        logger.info("忘记密码页——验证输入的用户名是否正确");
        UsersInfo usersInfo = usersInfoMapper.selectByUsernameOrPhone(username);
        if (null == usersInfo) {
            throw new Exception("您输入的用户名或者手机号有误,请检查后重新输入");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",usersInfo.getUsername());
        jsonObject.put("phone", ValidateUtil.validateMobile(usersInfo.getPhone()));

        return jsonObject;
    }
}
