package com.frog.server.controller.admin;

import com.frog.common.result.Result;
import com.frog.pojo.vo.OrderReportVO;
import com.frog.pojo.vo.SalesTop10ReportVO;
import com.frog.pojo.vo.TurnoverReportVO;
import com.frog.pojo.vo.UserReportVO;
import com.frog.server.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计");
        return Result.success(reportService.turnoverStatistics(start, end));
    }

    /**
     * 用户统计
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("用户统计");
        return Result.success(reportService.userStatistics(start, end));
    }

    /**
     * 订单统计
     */
    @GetMapping("/orderStatistics")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计");
        return Result.success(reportService.orderStatistics(start, end));
    }

    /**
     * Top10热门统计
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("Top10热门统计");
        return Result.success(reportService.getSalesTop10(start, end));
    }
}
