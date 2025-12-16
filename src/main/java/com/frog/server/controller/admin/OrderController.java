package com.frog.server.controller.admin;


import com.frog.common.result.PageResult;
import com.frog.common.result.Result;
import com.frog.pojo.dto.OrdersCancelDTO;
import com.frog.pojo.dto.OrdersPageQueryDTO;
import com.frog.pojo.dto.OrdersRejectionDTO;
import com.frog.pojo.vo.OrderStatisticsVO;
import com.frog.pojo.vo.OrderVO;
import com.frog.server.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单分页查询
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单分页查询: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 各个状态订单数量统计
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        log.info("各个状态订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单详情查询
     */
    @GetMapping("/detail")
    public Result<OrderVO> getOrderDetail(Long orderId) {
        log.info("订单详情查询: {}", orderId);
        OrderVO orderVO = orderService.getOrderDetail(orderId);
        return Result.success(orderVO);
    }

    /**
     * 拒单
     */
    @PutMapping("/rejection")
    public Result reject(OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单: {}", ordersRejectionDTO);
        try {
            orderService.reject(ordersRejectionDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel")
    public Result cancel(OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单: {}", ordersCancelDTO);
        try {
            orderService.adminCancelOrder(ordersCancelDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }
}
