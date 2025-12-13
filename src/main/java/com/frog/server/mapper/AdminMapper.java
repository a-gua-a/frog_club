package com.frog.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.frog.pojo.entity.Admin;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("select * from admins where username = #{username}")
    Admin getByUsername(String username);
}
