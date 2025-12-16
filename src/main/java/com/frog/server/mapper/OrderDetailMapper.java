package com.frog.server.mapper;

import com.frog.pojo.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单详情
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);

     /**
      * 根据订单id查询订单明细
      * @param orderId
      * @return
      */
     @Select("select * from order_detail where order_id = #{orderId}")
     List<OrderDetail> listByOrderId(Long orderId);

     /**
      * 批量更新订单详情
      * @param orderDetailList
      */
    void updateBatch(List<OrderDetail> orderDetailList);
}
