package com.frog.server.mapper;

import com.frog.pojo.dto.GoodsSalesDTO;
import com.frog.pojo.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取销售Top10商品
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(Map<String, Object> map);
}
