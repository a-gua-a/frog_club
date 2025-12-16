package com.frog.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.frog.common.constant.MessageConstant;
import com.frog.common.context.BaseContext;
import com.frog.common.exception.MessageBookBusinessException;
import com.frog.common.exception.OrderBusinessException;
import com.frog.common.exception.PreOrderBusinessException;
import com.frog.common.result.PageResult;
import com.frog.common.utils.WeChatPayUtil;
import com.frog.pojo.dto.*;
import com.frog.pojo.entity.*;
import com.frog.pojo.vo.OrderPaymentVO;
import com.frog.pojo.vo.OrderStatisticsVO;
import com.frog.pojo.vo.OrderSubmitVO;
import com.frog.pojo.vo.OrderVO;
import com.frog.server.mapper.*;
import com.frog.server.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private PreOrderMapper preOrderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private MessageBookMapper messageBookMapper;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        Long userId = BaseContext.getCurrentId();
        PreOrder preOrder = new PreOrder();
        preOrder.setUserId(userId);
        //异常情况的处理（收货地址为空、购物车为空）
        MessageBook messageBook = messageBookMapper.getById(ordersSubmitDTO.getMessageBookId());
        if (messageBook == null) {
            throw new MessageBookBusinessException(MessageConstant.MESSAGE_BOOK_IS_NULL);
        }

        //查询当前用户的待确认订单数据
        List<PreOrder> preOrderList = preOrderMapper.list(preOrder);
        if (preOrderList == null || preOrderList.size() == 0) {
            throw new PreOrderBusinessException(MessageConstant.PRE_ORDER_IS_NULL);
        }

        //构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setPhone(messageBook.getPhone());
        order.setEmployeeId(messageBook.getEmployeeId());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        //向订单表插入1条数据
        orderMapper.insert(order);

        //订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (PreOrder item : preOrderList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        //向明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetailList);

        //清理购物车中的数据
        preOrderMapper.deleteByUserId(userId);

        //封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "阿瓜电竞订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 历史订单查询
     */
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        // 设置当前用户id
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        // 调用mapper查询分页数据
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        // 封装分页结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 订单详情查询
     */
    @Override
    public OrderVO getOrderDetail(Long orderId) {
        // 根据订单id查询订单
        Orders orders = orderMapper.getById(orderId);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 根据订单id查询订单明细
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(orderId);
        Long userId = BaseContext.getCurrentId();
        String userName = userMapper.getById(userId).getName();
        // 封装订单VO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        orderVO.setUserName(userName);
        orderVO.setUserId(userId);
        return orderVO;
    }

     /**
      * 订单取消
      */
     @Override
     public void userCancelOrder(Long orderId) throws Exception {
         // 根据id查询订单
         Orders ordersDB = orderMapper.getById(orderId);

         // 校验订单是否存在
         if (ordersDB == null) {
             throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
         }

         //订单状态 1待付款 2待接单 3已接单 4已完成 5已取消 6已退款
         if (ordersDB.getStatus() > 2) {
             throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
         }

         Orders orders = new Orders();
         orders.setId(ordersDB.getId());

         // 订单处于待接单状态下取消，需要进行退款
         if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
             //调用微信支付退款接口
             weChatPayUtil.refund(
                     ordersDB.getNumber(), //商户订单号
                     ordersDB.getNumber(), //商户退款单号
                     new BigDecimal(0.01),//退款金额，单位 元
                     new BigDecimal(0.01));//原订单金额

             //支付状态修改为 退款
             orders.setPayStatus(Orders.REFUND);
         }

         // 更新订单状态、取消原因、取消时间
         orders.setStatus(Orders.CANCELLED);
         orders.setCancelReason("用户取消");
         orders.setCancelTime(LocalDateTime.now());
         orderMapper.update(orders);
     }

     /**
      * 再来一单
      */
     @Override
     public void reorder(Long orderId) {
         // 查询当前用户id
         Long userId = BaseContext.getCurrentId();

         // 根据订单id查询当前订单详情
         List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(orderId);

         // 将订单详情对象转换为购物车对象
         List<PreOrder> preOrderList = orderDetailList.stream().map(x -> {
             PreOrder preOrder = new PreOrder();

             // 将原订单详情里面的菜品信息重新复制到购物车对象中
             BeanUtils.copyProperties(x, preOrder, "id");
             preOrder.setUserId(userId);
             preOrder.setCreateTime(LocalDateTime.now());

             return preOrder;
         }).collect(Collectors.toList());

         // 将购物车对象批量添加到数据库
         preOrderMapper.insertBatch(preOrderList);
     }

      /**
       * 各个状态订单数量统计
       */
    @Override
    public OrderStatisticsVO statistics() {
         // 调用mapper查询订单统计数据
        Integer toBeConfirmedCount = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer inProgressCount = orderMapper.countStatus(Orders.IN_PROGRESS);
        Integer completeCount = orderMapper.countStatus(Orders.COMPLETED);
        // 封装订单统计VO
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmedCount);
        orderStatisticsVO.setInProgress(inProgressCount);
        orderStatisticsVO.setCompleted(completeCount);
        return orderStatisticsVO;
    }

    /**
      * 接单
      */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        // 根据订单id查询订单
        Orders orders = orderMapper.getById(ordersConfirmDTO.getId());
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 校验订单状态
        if (orders.getStatus() != Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 更新订单状态
        Orders updatedOrders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.IN_PROGRESS)
                .build();
        orderMapper.update(updatedOrders);

        // 记录接单时间
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(ordersConfirmDTO.getId());
        orderDetailList.forEach(x -> {
            x.setStartTime(LocalDateTime.now());
        });
        orderDetailMapper.updateBatch(orderDetailList);
    }

    /**
     * 拒单
     */
    @Override
    public void reject(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        /// 根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        // 订单只有存在且状态为2（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
            //用户已支付，需要退款
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }

        // 拒单需要退款，根据订单id更新订单状态、拒单原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
      * 取消订单
      */
    @Override
    public void adminCancelOrder(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        //支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
            //用户已支付，需要退款
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }

        // 管理端取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 完成订单
     */
    @Override
    public void complete(Long orderId) {
        // 根据订单id查询订单
        Orders ordersDB = orderMapper.getById(orderId);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 校验订单状态
        if (ordersDB.getStatus() != Orders.IN_PROGRESS) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 更新订单状态
        Orders updatedOrders = Orders.builder()
                .id(orderId)
                .status(Orders.COMPLETED)
                .build();
        orderMapper.update(updatedOrders);
        // 记录结单时间
        List<OrderDetail> orderDetailList = orderDetailMapper.listByOrderId(orderId);
        orderDetailList.forEach(x -> {
            x.setEndTime(LocalDateTime.now());
        });
        orderDetailMapper.updateBatch(orderDetailList);

    }

}
