package com.frog.server.mapper;

import com.frog.pojo.entity.Services;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ServicesMapper {

    /**
     * 根据id查询服务
     */
    @Select("select * from service where id = #{id}")
    Services selectById(Long id);
}
