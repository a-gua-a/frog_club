package com.frog.server.service;

import com.frog.pojo.vo.OrderReportVO;
import com.frog.pojo.vo.SalesTop10ReportVO;
import com.frog.pojo.vo.TurnoverReportVO;
import com.frog.pojo.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    TurnoverReportVO turnoverStatistics(LocalDate start, LocalDate end);

    UserReportVO userStatistics(LocalDate start, LocalDate end);

    OrderReportVO orderStatistics(LocalDate start, LocalDate end);

    SalesTop10ReportVO getSalesTop10(LocalDate start, LocalDate end);
}
