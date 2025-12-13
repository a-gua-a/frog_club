package com.frog.server.mapper;

import com.frog.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {

    /**
     * 根据id查询分类
     */
    @Select("select * from category where id = #{id}")
    Category selectById(Long id);
}
