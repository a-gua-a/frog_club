package com.frog.server.mapper;

import com.frog.pojo.dto.OrdersPageQueryDTO;
import com.frog.pojo.entity.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

     /**
      * 分页查询订单
      */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询
     */
     @Select("select * from orders where id = #{orderId}")
     Orders getById(Long orderId);

     /**
      * 根据状态统计订单数量
      */
    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 条件查询订单
     */
     List<Orders> list(Orders orders);
}
