package com.frog.server.task;

import com.frog.pojo.entity.Orders;
import com.frog.server.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron="0 * * * * ?")//每分钟的第0秒执行
    public void handleTimeoutOrder(){
        log.info("处理超时订单");
        LocalDateTime ddl = LocalDateTime.now().minusMinutes(2);
        Orders order = Orders.builder()
                .status(Orders.PENDING_PAYMENT)
                .orderTime(ddl)
                .build();
        List<Orders> orders = orderMapper.list(order);
        if(orders == null || orders.isEmpty()){
            log.info("没有超时订单");
        }else{
            for(Orders o : orders){
                o.setStatus(Orders.CANCELLED);
                o.setCancelReason("订单超时未支付");
                orderMapper.update(o);
            }
        }
    }

}
