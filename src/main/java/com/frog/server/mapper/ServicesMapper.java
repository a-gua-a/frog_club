package com.frog.server.mapper;

import com.frog.common.constant.AutoFillConstant;
import com.frog.common.enumeration.OperationType;
import com.frog.pojo.dto.ServicesPageQueryDTO;
import com.frog.pojo.entity.Services;
import com.frog.pojo.vo.ServicesVO;
import com.frog.server.annotation.AutoFill;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ServicesMapper {

    /**
     * 根据id查询服务
     */
    @Select("select * from service where id = #{id}")
    Services selectById(Long id);

    /**
     * 新增服务
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Services services);

     /**
      * 更新服务
      */
    @AutoFill(value = OperationType.UPDATE)
    void update(Services services);

     /**
      * 批量删除服务
      */
    void deleteBatch(List<Long> ids);

     /**
      * 分页查询服务
      */
    Page<ServicesVO> pageQuery(ServicesPageQueryDTO servicesPageQueryDTO);

     /**
      * 根据分类id查询服务
      */
    List<Services> list(Services services);

     /**
      * 根据分类id查询服务数量
      */
     @Select("select count(*) from service where category_id = #{id}")
     Integer countByCategoryId(Long id);
}
