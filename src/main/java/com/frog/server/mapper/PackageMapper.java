package com.frog.server.mapper;

import com.frog.common.enumeration.OperationType;
import com.frog.pojo.entity.Pack;
import com.frog.pojo.vo.ServiceItemVO;
import com.frog.server.annotation.AutoFill;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PackageMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from package where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

     /**
      * 新增套餐
      */
     @AutoFill(value = OperationType.INSERT)
     void insert(Pack pack);


    /**
     * 批量删除套餐
     */
    void deleteBatch(List<Long> ids);

     /**
      * 根据id查询套餐
      */
    @Select("select * from package where id = #{id}")
    Pack getById(Long id);

    /**
     * 更新套餐
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Pack pack);

    /**
     * 分页查询套餐
     */
    Page<Pack> pageQuery(Pack pack);

    /**
     * 动态条件查询套餐
     */
    List<Pack> list(Pack pack);

    /**
     * 根据套餐id查询菜品选项
     * @param packageId
     * @return
     */
    @Select("select ps.name, ps.copies, s.image, s.description " +
            "from package_service ps left join service s on ps.service_id = s.id " +
            "where ps.package_id = #{packageId}")
    List<ServiceItemVO> getServiceItemByPackageId(Long packageId);

    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
