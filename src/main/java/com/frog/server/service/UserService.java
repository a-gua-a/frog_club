package com.frog.server.service;


import com.frog.pojo.dto.UserLoginDTO;
import com.frog.pojo.entity.User;

public interface
UserService {

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
}
