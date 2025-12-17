package com.frog.server.service.impl;

import com.frog.common.result.PageResult;
import com.frog.pojo.dto.ServicesDTO;
import com.frog.pojo.dto.ServicesPageQueryDTO;
import com.frog.pojo.entity.Category;
import com.frog.pojo.entity.ServiceDetail;
import com.frog.pojo.entity.Services;
import com.frog.pojo.vo.ServicesVO;
import com.frog.server.mapper.CategoryMapper;
import com.frog.server.mapper.ServiceDetailMapper;
import com.frog.server.mapper.ServicesMapper;
import com.frog.server.service.ServicesService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    /**
     * 新增服务
     */
    @Override
    public void save(ServicesDTO servicesDTO) {
        Services services = new Services();
        BeanUtils.copyProperties(servicesDTO, services);
        servicesMapper.insert(services);
        // 新增服务详情
        List<ServiceDetail> serviceDetails = servicesDTO.getServiceDetails();
        if (CollectionUtils.isNotEmpty(serviceDetails)) {
            serviceDetails.forEach(serviceDetail -> {
                serviceDetail.setServiceId(services.getId());
            });
            serviceDetailMapper.insertBatch(serviceDetails);
        }
    }

     /**
      * 更新服务
      */
    @Override
    public void update(ServicesDTO servicesDTO) {
        Services services = new Services();
        BeanUtils.copyProperties(servicesDTO, services);
        servicesMapper.update(services);
        // 先删除原有的服务详情
        serviceDetailMapper.deleteByServiceId(services.getId());
        // 新增服务详情
        List<ServiceDetail> serviceDetails = servicesDTO.getServiceDetails();
        if (CollectionUtils.isNotEmpty(serviceDetails)) {
            serviceDetails.forEach(serviceDetail -> {
                serviceDetail.setServiceId(services.getId());
            });
            serviceDetailMapper.insertBatch(serviceDetails);
        }
    }

     /**
      * 批量删除服务
      */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 删除菜品
        servicesMapper.deleteBatch(ids);
        // 删除服务详情
        ids.forEach(id -> {
            serviceDetailMapper.deleteByServiceId(id);
        });
    }

    /**
     * 分页查询服务
     */
    @Override
    public PageResult page(ServicesPageQueryDTO servicesPageQueryDTO) {
        PageHelper.startPage(servicesPageQueryDTO.getPage(), servicesPageQueryDTO.getPageSize());
        // 查询服务
        Page<ServicesVO> servicesList = servicesMapper.pageQuery(servicesPageQueryDTO);
        return new PageResult(servicesList.getTotal(), servicesList.getResult());
    }

     /**
      * 根据分类id查询服务
      */
    @Override
    public List<Services> getByCategoryId(Long categoryId) {
        Services services = Services.builder()
                .categoryId(categoryId)
                .build();
        List<Services> servicesList = servicesMapper.list(services);
        return servicesList;
    }

    @Override
    public List<ServicesVO> listWithDetail(Services services) {
        List<Services> servicesList = servicesMapper.list(services);

        List<ServicesVO> servicesVOList = new ArrayList<>();

        for (Services s : servicesList) {
            ServicesVO servicesVO = new ServicesVO();
            BeanUtils.copyProperties(s,servicesVO);

            //根据服务id查询对应的服务详情
            List<ServiceDetail> serviceDetails = serviceDetailMapper.selectByServiceId(s.getId());

            servicesVO.setServiceDetails(serviceDetails);
            servicesVOList.add(servicesVO);
        }

        return servicesVOList;
    }
}
