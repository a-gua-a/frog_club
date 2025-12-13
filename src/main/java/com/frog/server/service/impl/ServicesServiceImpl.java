package com.frog.server.service.impl;

import com.frog.pojo.entity.Category;
import com.frog.pojo.entity.ServiceDetail;
import com.frog.pojo.entity.Services;
import com.frog.pojo.vo.ServicesVO;
import com.frog.server.mapper.CategoryMapper;
import com.frog.server.mapper.ServiceDetailMapper;
import com.frog.server.mapper.ServicesMapper;
import com.frog.server.service.ServicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServicesServiceImpl implements ServicesService {

    @Autowired
    private ServicesMapper servicesMapper;

    @Autowired
    private ServiceDetailMapper serviceDetailMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据id查询服务
     */
    @Override
    public ServicesVO getById(Long id) {
        // 查询菜品
        Services services = servicesMapper.selectById(id);
        // 查询服务口味
        List<ServiceDetail> serviceDetails = serviceDetailMapper.selectByServiceId(id);
        // 查询分类
        Category category = categoryMapper.selectById(services.getCategoryId());
        // 封装VO
        ServicesVO servicesVO = new ServicesVO();
        BeanUtils.copyProperties(services, servicesVO);
        servicesVO.setServiceDetails(serviceDetails);
        servicesVO.setCategoryName(category.getName());
        return servicesVO;
    }
}
