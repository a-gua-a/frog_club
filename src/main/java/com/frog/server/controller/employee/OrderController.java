package com.frog.server.controller.employee;


import com.frog.common.result.PageResult;
import com.frog.common.result.Result;
import com.frog.pojo.dto.OrdersConfirmDTO;
import com.frog.pojo.dto.OrdersPageQueryDTO;
import com.frog.pojo.vo.OrderVO;
import com.frog.server.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("employeeOrderController")
@RequestMapping("/employee/order")
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
     * 订单详情查询
     */
    @GetMapping("/detail")
    public Result<OrderVO> getOrderDetail(Long orderId) {
        log.info("订单详情查询: {}", orderId);
        OrderVO orderVO = orderService.getOrderDetail(orderId);
        return Result.success(orderVO);
    }

    /**
     * 接单
     */
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单: {}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }


    /**
     * 完成订单
     */
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        log.info("完成订单: {}", id);
        orderService.complete(id);
        return Result.success();
    }
}
