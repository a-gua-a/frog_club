package com.frog.server.controller.user;

import com.frog.common.result.PageResult;
import com.frog.common.result.Result;
import com.frog.pojo.dto.OrdersPageQueryDTO;
import com.frog.pojo.dto.OrdersPaymentDTO;
import com.frog.pojo.dto.OrdersSubmitDTO;
import com.frog.pojo.vo.OrderPaymentVO;
import com.frog.pojo.vo.OrderSubmitVO;
import com.frog.pojo.vo.OrderVO;
import com.frog.server.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单查询
     */
     @GetMapping("/historyOrders")
     public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
         log.info("历史订单查询：{}", ordersPageQueryDTO);
         PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
         return Result.success(pageResult);
     }

     /**
      * 订单详情查询
      */
     @GetMapping("/detail/{id}")
     public Result<OrderVO> detail(@PathVariable("id") Long OrderId) {
         log.info("订单详情查询：{}", OrderId);
         OrderVO orderVO = orderService.getOrderDetail(OrderId);
         return Result.success(orderVO);
     }

     /**
      * 订单取消
      */
     @PutMapping("/cancel/{id}")
     public Result cancel(@PathVariable("id") Long OrderId) {
         log.info("订单取消：{}", OrderId);
         try {
             orderService.userCancelOrder(OrderId);
         } catch (Exception e) {
             return Result.error(e.getMessage());
         }
         return Result.success();
     }

    /**
     * 再来一单
     */
     @PostMapping("/repetition/{id}")
     public Result reorder(@PathVariable("id") Long OrderId) {
         log.info("再来一单：{}", OrderId);
         orderService.reorder(OrderId);
         return Result.success();
     }

    /**
     * 用户催单
     */
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable("id") Long OrderId) {
        log.info("用户催单：{}", OrderId);
        orderService.reminder(OrderId);
        return Result.success();
    }
}
