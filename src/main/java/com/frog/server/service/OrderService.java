package com.frog.server.service;

import com.frog.common.result.PageResult;
import com.frog.pojo.dto.*;
import com.frog.pojo.vo.OrderPaymentVO;
import com.frog.pojo.vo.OrderStatisticsVO;
import com.frog.pojo.vo.OrderSubmitVO;
import com.frog.pojo.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单查询
     */
    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单详情查询
     */
    OrderVO getOrderDetail(Long orderId);

    /**
     * 订单取消
     */
    void userCancelOrder(Long orderId) throws Exception;

    /**
     * 再来一单
     */
    void reorder(Long orderId);

    /**
     * 各个状态订单数量统计
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

     /**
      * 拒单
      */
    void reject(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 取消订单
     */
    void adminCancelOrder(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     */
    //void deliver(Long orderId);

     /**
      * 完成订单
      */
    void complete(Long orderId);

    /**
     * 用户催单
     */
    void reminder(Long orderId);
}
