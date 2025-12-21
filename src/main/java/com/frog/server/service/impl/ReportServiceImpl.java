package com.frog.server.service.impl;

import com.frog.pojo.dto.GoodsSalesDTO;
import com.frog.pojo.entity.Orders;
import com.frog.pojo.vo.OrderReportVO;
import com.frog.pojo.vo.SalesTop10ReportVO;
import com.frog.pojo.vo.TurnoverReportVO;
import com.frog.pojo.vo.UserReportVO;
import com.frog.server.mapper.OrderDetailMapper;
import com.frog.server.mapper.OrderMapper;
import com.frog.server.mapper.UserMapper;
import com.frog.server.service.ReportService;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate start, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(start);
        while (!start.equals(end)) {
            start = start.plusDays(1);
            dateList.add(start);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumTurnover(map);
            if (turnover == null) {
                turnover = 0.0;
            }
            turnoverList.add(turnover);
        }
        String dateListStr = StringUtil.join(",", dateList);
        String turnoverListStr = StringUtil.join(",", turnoverList);
        return TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(turnoverListStr)
                .build();
    }

    @Override
    public UserReportVO userStatistics(LocalDate start, LocalDate end) {

        // 生成日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(start);
        while (!start.equals(end)) {
            start = start.plusDays(1);
            dateList.add(start);
        }

        // 统计用户总量和新增用户
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("endTime", endTime);
            Integer totalUser = userMapper.countUser(map);
            if (totalUser == null) {
                totalUser = 0;
            }
            map.put("startTime", startTime);
            Integer newUser = userMapper.countUser(map);
            if (newUser == null) {
                newUser = 0;
            }
            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }
        String dateListStr = StringUtil.join(",", dateList);
        String totalUserListStr = StringUtil.join(",", totalUserList);
        String newUserListStr = StringUtil.join(",", newUserList);
        return UserReportVO.builder()
                .dateList(dateListStr)
                .totalUserList(totalUserListStr)
                .newUserList(newUserListStr)
                .build();
    }

    @Override
    public OrderReportVO orderStatistics(LocalDate start, LocalDate end) {
        // 生成日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(start);
        while (!start.equals(end)) {
            start = start.plusDays(1);
            dateList.add(start);
        }

        // 统计订单数量和有效订单数量
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            Integer orderCount = orderMapper.countOrder(map);
            if (orderCount == null) {
                orderCount = 0;
            }
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countOrder(map);
            if (validOrderCount == null) {
                validOrderCount = 0;
            }
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        // 计算订单完成率
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).orElse(0);
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).orElse(0);
        Double orderCompletionRate = 0.0;
        if (totalOrderCount > 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        //合并数据
        String dateListStr = StringUtil.join(",", dateList);
        String orderCountListStr = StringUtil.join(",", orderCountList);
        String validOrderCountListStr = StringUtil.join(",", validOrderCountList);
        return OrderReportVO.builder()
                .dateList(dateListStr)
                .orderCountList(orderCountListStr)
                .validOrderCountList(validOrderCountListStr)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate start, LocalDate end) {
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("status", Orders.COMPLETED);
        List<GoodsSalesDTO> goodsSalesDTOList = orderDetailMapper.getSalesTop10(map);
        // 转换为VO
        List<String> nameList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).toList();
        List<Integer> numberList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).toList();
        return SalesTop10ReportVO.builder()
                .nameList(StringUtil.join(",", nameList))
                .numberList(StringUtil.join(",", numberList))
                .build();
    }
}
