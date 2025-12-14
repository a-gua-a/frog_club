package com.frog.server.mapper;

import com.frog.pojo.entity.ServiceDetail;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 批量新增服务详情
     */
    void insertBatch(List<ServiceDetail> serviceDetails);

    /**
     * 根据服务id删除服务详情
     */
    @Delete("delete from service_detail where service_id = #{id}")
    void deleteByServiceId(Long id);
}
