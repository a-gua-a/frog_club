package com.frog.server.mapper;

import com.frog.pojo.entity.PackageServices;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PackageServiceMapper {
        /**
         * 新增套餐菜品
         */
        void insert(PackageServices packageServices);


        /**
         * 根据套餐id删除套餐菜品
         */
        void deleteByPackageId(Long id);
}
