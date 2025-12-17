package com.frog.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.frog.common.exception.LoginFailedException;
import com.frog.common.properties.WeChatProperties;
import com.frog.common.utils.HttpClientUtil;
import com.frog.pojo.dto.UserLoginDTO;
import com.frog.pojo.entity.User;
import com.frog.server.mapper.UserMapper;
import com.frog.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties properties;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {

        String code = userLoginDTO.getCode();
        String openid = getOpenid(code);

        if(openid==null){
            throw new LoginFailedException("登录失败");
        }

        User user = userMapper.selectByOpenid(openid);
        if(user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    private String getOpenid(String code){
        Map<String,String> map = new HashMap<>();
        map.put("appid", properties.getAppid());
        map.put("secret", properties.getSecret());
        map.put("grant_type", "authorization_code");
        map.put("js_code", code);
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
