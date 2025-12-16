package com.frog.server.mapper;

import com.frog.pojo.entity.PreOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PreOrderMapper {

    /**
      * 查看购物车
     * @param preOrder
      */
     List<PreOrder> list(PreOrder preOrder);

    /**
     * 添加购物车
     * @param preOrder
     */
    void insert(PreOrder preOrder);

     /**
      * 更新购物车
      * @param preOrder
      */
    void update(PreOrder preOrder);

     /**
      * 删除购物车
      * @param preOrder
      */
    void delete(PreOrder preOrder);

    /**
      * 删除用户购物车
      * @param userId
      */
    void deleteByUserId(Long userId);


    /**
     * 批量添加购物车
     * @param preOrderList
     */
    void insertBatch(List<PreOrder> preOrderList);
}
