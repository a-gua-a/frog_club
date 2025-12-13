package com.frog.server.service;

import com.frog.pojo.vo.ServicesVO;

public interface ServicesService {

     /**
     * 根据id查询服务
     */
    ServicesVO getById(Long id);
}
