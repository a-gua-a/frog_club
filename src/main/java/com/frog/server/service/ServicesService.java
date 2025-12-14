package com.frog.server.service;

import com.frog.common.result.PageResult;
import com.frog.pojo.dto.ServicesDTO;
import com.frog.pojo.dto.ServicesPageQueryDTO;
import com.frog.pojo.entity.Services;
import com.frog.pojo.vo.ServicesVO;

import java.util.List;

public interface ServicesService {

     /**
     * 根据id查询服务
     */
    ServicesVO getById(Long id);

     /**
     * 新增服务
     */
    void save(ServicesDTO servicesDTO);

    /**
     * 更新服务
     */
    void update(ServicesDTO servicesDTO);

     /**
     * 删除服务
     */
    void deleteBatch(List<Long> ids);

    /**
     * 分页查询服务
     */
    PageResult page(ServicesPageQueryDTO servicesPageQueryDTO);

     /**
     * 根据分类id查询服务
     */
    List<Services> getByCategoryId(Long categoryId);
}
