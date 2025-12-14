package com.frog.server.service;


import com.frog.common.result.PageResult;
import com.frog.pojo.dto.PackageDTO;
import com.frog.pojo.dto.PackagePageQueryDTO;
import com.frog.pojo.entity.Pack;
import com.frog.pojo.entity.PackageServices;
import com.frog.pojo.vo.ServiceItemVO;

import java.util.List;

public interface PackageService {
        /**
         * 新增套餐
         * @param packageDTO
         */
        void insert(PackageDTO packageDTO);


        /**
         * 批量删除套餐
         */
        void deleteBatch(List<Long> ids);

        /**
         * 套餐起售/停售
         */
        void updateStatus(Integer status, Long id);

        /**
         * 更新套餐
         */
        void update(PackageDTO packageDTO);

        /**
         * 分页查询套餐
         */
        PageResult page(PackagePageQueryDTO packagePageQueryDTO);

        /**
        * 条件查询
        * @param pack
        * @return
         */
        List<Pack> list(Pack pack);

        /**
        * 根据id查询服务项选项
        * @param id
         * @return
         */
        List<ServiceItemVO> getServiceItemById(Long id);
}
