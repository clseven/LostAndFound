package com.lostfound.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lostfound.common.exception.BaseException;
import com.lostfound.common.properties.WeChatProperties;
import com.lostfound.common.utils.HttpClientUtil;
import com.lostfound.domain.dto.UserLoginDTO;
import com.lostfound.domain.dto.UserUpdateDTO;
import com.lostfound.domain.po.User;
import com.lostfound.common.exception.LoginFailedException;
import com.lostfound.domain.vo.UserUpdateVO;
import com.lostfound.mapper.UserMapper;
import com.lostfound.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 失物招领小程序用户表 服务实现类
 * </p>
 *
 * @author clseven
 * @since 2025-10-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {


        //调用微信接口服务，获得当前用户的openid
        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空，为空登录失败
        if(openid==null){
            throw new LoginFailedException("登录失败");
        }

        // 构建查询条件：where openid = ?
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid); // eq 表示 "等于" 条件

        // 调用 MP 自带的 selectOne 方法
        //判断是否新用户
        User user = userMapper.selectOne(queryWrapper);;


        //新用户自动注册
        if(user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    @Override
    public UserUpdateVO updateUserInfo(UserUpdateDTO userUpdateDTO) {
        User user = this.getById(userUpdateDTO.getId()); // 用id查询
        if (user == null) {
            throw new BaseException("用户不存在");
        }

        if (StringUtils.isNotBlank(userUpdateDTO.getName())) {
            user.setName(userUpdateDTO.getName());
        }
        if (StringUtils.isNotBlank(userUpdateDTO.getPhone())) {
            user.setPhone(userUpdateDTO.getPhone());
        }
        if (StringUtils.isNotBlank(userUpdateDTO.getSex())) {
            if (!"0".equals(userUpdateDTO.getSex()) && !"1".equals(userUpdateDTO.getSex())) {
                throw new BaseException("性别只能是0（女）或1（男）");
            }
            user.setSex(userUpdateDTO.getSex());
        }
        if (StringUtils.isNotBlank(userUpdateDTO.getAvatar())) {
            user.setAvatar(userUpdateDTO.getAvatar());
        }

        // 4. 保存修改
        userMapper.updateById(user);

        // 5. 转换为VO返回
        return UserUpdateVO.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .sex(user.getSex())
                .avatar(user.getAvatar())
                .build();
    }

    private String getOpenid(String code) {
        Map<String,String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");

        return openid;
    }
}
