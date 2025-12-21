package com.frog.server.service.impl;

import com.frog.common.constant.StatusConstant;
import com.frog.pojo.entity.Orders;
import com.frog.pojo.vo.BusinessDataVO;
import com.frog.pojo.vo.OrderOverViewVO;
import com.frog.pojo.vo.PackageOverViewVO;
import com.frog.pojo.vo.ServicesOverViewVO;
import com.frog.server.mapper.OrderMapper;
import com.frog.server.mapper.PackageMapper;
import com.frog.server.mapper.ServicesMapper;
import com.frog.server.mapper.UserMapper;
import com.frog.server.service.WorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ServicesMapper servicesMapper;
    @Autowired
    private PackageMapper packageMapper;

    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /**
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 订单完成率：有效订单数 / 总订单数
         * 平均客单价：营业额 / 有效订单数
         * 新增用户：当日新增用户的数量
         */

        Map<String,Object> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);

        //查询总订单数
        Integer totalOrderCount = orderMapper.countOrder(map);

        map.put("status", Orders.COMPLETED);
        //营业额
        Double turnover = orderMapper.sumTurnover(map);
        turnover = turnover == null? 0.0 : turnover;

        //有效订单数
        Integer validOrderCount = orderMapper.countOrder(map);

        Double unitPrice = 0.0;

        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }

        //新增用户数
        Integer newUsers = userMapper.countUser(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    /**
     * 查询订单管理数据
     *
     * @return
     */
    public OrderOverViewVO getOrderOverView() {
        Map<String,Object> map = new HashMap<>();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", Orders.TO_BE_CONFIRMED);

        //待接单
        Integer waitingOrders = orderMapper.countOrder(map);

        //进行中订单
        map.put("status", Orders.IN_PROGRESS);
        Integer deliveredOrders = orderMapper.countOrder(map);

        //已完成
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countOrder(map);

        //已取消
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countOrder(map);

        //全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countOrder(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .inProgressOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询服务总览
     *
     * @return
     */
    public ServicesOverViewVO getServicesOverView() {
        Map<String,Object> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = servicesMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = servicesMapper.countByMap(map);

        return ServicesOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    public PackageOverViewVO getPackageOverView() {
        Map<String,Object> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = packageMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = packageMapper.countByMap(map);

        return PackageOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
