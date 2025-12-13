package com.frog.server.mapper;

import com.frog.pojo.entity.ServiceDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ServiceDetailMapper {

    /**
     * 根据服务id查询服务详情
     */
    @Select("select * from service_detail where service_id = #{serviceId}")
    List<ServiceDetail> selectByServiceId(Long serviceId);
}
